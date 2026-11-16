# Execução local

Este guia prepara o ambiente local do FoodTech Fraud Alerts para desenvolver a API e verificar sua conexão com o banco.

Ao concluir, você terá o PostgreSQL em execução pelo Docker Compose e a aplicação Spring Boot iniciada na sua máquina, conectada ao banco na porta `5433`.

## Escopo desta configuração

O `docker-compose.yml` atual inicia somente o PostgreSQL. A API roda localmente pelo Maven Wrapper. O fluxo completo descrito na arquitetura — incluindo SQS, worker, RDS e recursos AWS — ainda não é iniciado por este ambiente local.

A evolução do esquema do banco de dados é controlada pelo Flyway, por meio de migrações SQL versionadas. O Hibernate utiliza `ddl-auto: validate` para verificar a compatibilidade entre o mapeamento das entidades e a estrutura existente, sem alterar automaticamente o esquema.

No banco local existente, o histórico do Flyway já contém um registro de baseline, que estabelece uma versão de referência para o controle do esquema. Em uma instalação nova, sem histórico anterior, o Flyway executa as migrações SQL versionadas, começando por `V1__create_alerts_table.sql`.

A configuração atual não habilita `baseline-on-migrate` automaticamente. Portanto, antes de conectar a aplicação a um banco existente que ainda não possua histórico do Flyway, é necessário avaliar a estrutura e definir explicitamente a estratégia de baseline. O Hibernate utiliza `ddl-auto: validate` para verificar a compatibilidade entre as entidades e o esquema, sem alterá-lo.

## Pré-requisitos

- Docker Desktop em execução, com Docker Compose v2 disponível.
- Java 21 instalado.
- PowerShell no Windows. Em outros sistemas, use o shell equivalente para definir as mesmas variáveis de ambiente.

O Maven Wrapper do projeto está em `api-service`; não é necessário instalar o Maven separadamente.

## 1. Prepare as variáveis locais

Na raiz do repositório, crie seu arquivo local a partir do exemplo:

```powershell
Copy-Item .env.example .env
```

Abra `.env` e substitua `replace-with-a-local-password` por uma senha de desenvolvimento. Use a mesma senha em `POSTGRES_PASSWORD` e `DB_PASSWORD`:

```dotenv
POSTGRES_DB=foodtech
POSTGRES_USER=foodtech
POSTGRES_PASSWORD=<senha-local>

DB_URL=jdbc:postgresql://localhost:5433/foodtech
DB_USERNAME=foodtech
DB_PASSWORD=<senha-local>
```

O `.env` contém configuração local e não deve ser versionado. O `.gitignore` já o exclui; `.env.example` é o modelo seguro para compartilhar.

### O que cada variável configura

| Variável | Consumidor | Finalidade |
|---|---|---|
| `POSTGRES_DB` | Docker Compose / PostgreSQL | Nome do banco criado pelo contêiner. |
| `POSTGRES_USER` | Docker Compose / PostgreSQL | Usuário inicial do banco. |
| `POSTGRES_PASSWORD` | Docker Compose / PostgreSQL | Senha inicial do usuário do banco. |
| `DB_URL` | Spring Boot | Endereço JDBC usado pela aplicação no host. |
| `DB_USERNAME` | Spring Boot | Usuário usado pela aplicação para conectar ao banco. |
| `DB_PASSWORD` | Spring Boot | Senha usada pela aplicação para conectar ao banco. |

O Compose lê `.env` para substituir as variáveis usadas no `docker-compose.yml`. O Spring Boot não importa esse arquivo automaticamente: as variáveis `DB_*` precisam estar no ambiente do processo que inicia a aplicação.

O PostgreSQL usa `POSTGRES_*` para inicializar um diretório de dados vazio. Alterar esses valores no `.env` não troca automaticamente a senha de uma instância de banco já inicializada; nesses casos, atualize as credenciais no próprio banco ou reinicialize um ambiente descartável sabendo que isso pode afetar os dados locais.

## 2. Inicie o PostgreSQL

Na raiz do repositório, valide a configuração e inicie o serviço:

```powershell
docker compose config --quiet
docker compose up -d postgres
docker compose ps
```

Confirme que o banco está aceitando conexões:

```powershell
docker compose exec -T postgres pg_isready -U foodtech -d foodtech
```

O resultado esperado contém `accepting connections`. O PostgreSQL atende na porta `5433` do host e na porta `5432` dentro do contêiner.

## 3. Inicie a API no Windows

Abra uma sessão PowerShell na raiz do repositório. Carregue somente as variáveis que a aplicação precisa a partir de `.env`; os valores não são exibidos:

**Atenção:**  inicie a API em uma sessão de terminal separada daquela utilizada para executar comandos do Docker Compose e administrar o PostgreSQL. Mantenha a API em execução nessa segunda sessão enquanto realiza as verificações e chamadas HTTP na primeira.

```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^(DB_URL|DB_USERNAME|DB_PASSWORD)=(.*)$') {
        [Environment]::SetEnvironmentVariable($Matches[1], $Matches[2], 'Process')
    }
}
```

Na mesma sessão, inicie a aplicação:

```powershell
Set-Location api-service
.\mvnw.cmd spring-boot:run
```

As variáveis ficam disponíveis apenas nessa sessão e para os processos iniciados por ela. Se iniciar a aplicação pela IDE, configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` na configuração de execução da API, usando os valores locais de `.env`.

### Git Bash no Windows

Na raiz do repositório, carregue as variáveis de ambiente do arquivo `.env` e inicie a aplicação:

```bash
set -a
source .env
set +a

cd api-service
./mvnw spring-boot:run
```

O comando `source .env` carrega as variáveis do arquivo na sessão atual do Git Bash. O `set -a` faz com que elas sejam exportadas para os processos iniciados nessa sessão.

Esse procedimento disponibiliza tanto as variáveis `POSTGRES_*` quanto as `DB_*`, sem exibir seus valores no terminal.

As variáveis permanecem disponíveis somente na sessão atual e para os processos iniciados por ela.

Para encerrar a aplicação, pressione `Ctrl+C`.

### Linux e macOS (Bash)

Na raiz do repositório, defina a URL e o usuário. Digite a senha do `.env` quando solicitado; o modo silencioso evita mostrá-la no terminal:

```bash
export DB_URL='jdbc:postgresql://localhost:5433/foodtech'
export DB_USERNAME='foodtech'
read -r -s -p 'DB_PASSWORD: ' DB_PASSWORD
export DB_PASSWORD
printf '\n'
cd api-service
./mvnw spring-boot:run
```

As variáveis ficam disponíveis na sessão atual do terminal e para os processos iniciados por ela.

## 4. Verifique a aplicação

Quando a inicialização terminar, consulte o health check do Spring Boot Actuator:

```text
http://localhost:8080/actuator/health
```

Uma resposta com status `UP` indica que a aplicação iniciou. Para conferir o banco, verifique também que o comando `pg_isready` da etapa anterior informa que está aceitando conexões.

## Encerramento

Para interromper a API, use `Ctrl+C` no terminal em que ela está rodando.

Para parar e remover o contêiner do PostgreSQL:

```powershell
docker compose down
```

O PostgreSQL utiliza o volume nomeado `postgres_data`, declarado no `docker-compose.yml`. Por isso, os dados do banco permanecem armazenados no volume mesmo após a remoção do contêiner pelo comando `docker compose down`.

Para preservar os dados locais, não execute:

```powershell
docker compose down -v
```

A opção `-v` remove os volumes associados ao projeto e pode apagar os dados persistidos do PostgreSQL.

## Solução de problemas

| Sintoma | Causa provável | Como resolver |
|---|---|---|
| `Could not resolve placeholder 'DB_URL'` (ou `DB_USERNAME` / `DB_PASSWORD`) | A aplicação foi iniciada sem as variáveis no ambiente do processo. | Carregue as variáveis na sessão PowerShell ou configure-as na IDE e inicie a API novamente. |
| O Compose informa que `POSTGRES_*` não foi definida | `.env` não existe na raiz, tem outro nome ou não contém a variável exigida. | Crie `.env` a partir de `.env.example` e confira os nomes das variáveis. |
| A API não conecta ao banco | O contêiner ainda está inicializando, a porta está ocupada, as variáveis da aplicação não correspondem ao banco ou o diretório de dados já foi inicializado com outras credenciais. | Confirme `docker compose ps`, execute `pg_isready`, confira a porta `5433` e compare usuário, banco e senha. Se o banco já foi inicializado, alterar `.env` não redefine a senha existente. |
| A porta `5433` já está em uso | Outro processo está usando a porta publicada pelo Compose. | Libere a porta ou ajuste a porta do host no Compose e atualize `DB_URL` para corresponder. |
| A porta `8080` já está em uso | Outra aplicação ocupa a porta HTTP padrão da API. | Encerre o processo conflitante ou altere `server.port` na configuração local. Se alterar a porta, atualize também a URL do health check da seção 4 para utilizar a nova porta. |
