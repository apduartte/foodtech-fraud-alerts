# 🚀 FoodTech Fraud Alerts

Projeto de engenharia para construir uma plataforma de detecção e processamento de alertas de fraude, com arquitetura-alvo orientada a eventos na AWS.

![AWS](https://img.shields.io/badge/AWS-Cloud-%23FF9900?style=for-the-badge\&logo=amazonaws\&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-%23ED8B00?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/SpringBoot-Framework-%236DB33F?style=for-the-badge\&logo=springboot)
![Terraform](https://img.shields.io/badge/Terraform-IaC-%237B42BC?style=for-the-badge\&logo=terraform)
![Docker](https://img.shields.io/badge/Docker-Container-%230db7ed?style=for-the-badge\&logo=docker)

**Escopo tecnológico:** Os badges representam tecnologias utilizadas na arquitetura-alvo e no ambiente de desenvolvimento. Nem todas estão implementadas ou implantadas atualmente.
---

## 📌 Visão Geral

O **FoodTech Fraud Alerts** está em desenvolvimento para demonstrar decisões de arquitetura, implementação e operação de um sistema de alertas de fraude.

## Estado atual

A implementação disponível neste repositório é a base inicial da API Spring Boot conectada a PostgreSQL local. A arquitetura com ingestão de eventos, SQS, worker, RDS e implantação AWS é a direção planejada; esses componentes ainda não fazem parte do ambiente executável atual.

O [guia de execução local](docs/local-development.md) mostra como iniciar a API e o PostgreSQL.

---

## 🧠 Diretrizes Arquiteturais

* **Event-Driven Architecture**
* **Loose Coupling**
* **High Availability**
* **Scalability by Design**
* **Security by Default**
* **Observability First**

---

## 🏗️ Arquitetura-alvo

### 🔄 Fluxo de Alto Nível

```text
Client → API Gateway → Fraud Alerts API → SQS → Worker → RDS
                                          ↓
                                         DLQ
```

---

## 🧩 Componentes

### 🌐 Entrada

* API REST (Spring Boot)
* API Gateway (opcional)
* HTTPS (TLS via ACM)

---

### ⚙️ Processamento

* API Service (ingestão de eventos)
* Worker Service (processamento assíncrono)
* Fila (SQS Standard)

---

### 🗄️ Dados

* Amazon RDS PostgreSQL (transacional)
* Amazon S3 (opcional – histórico / data lake)

---

### 🔐 Segurança

* IAM (princípio do menor privilégio)
* AWS SSM / Secrets Manager (gestão de segredos)
* Criptografia em trânsito (TLS)
* Criptografia em repouso (RDS)

---

### 📊 Observabilidade

* Logs estruturados (JSON)
* CloudWatch Logs
* Métricas (latência, erro, throughput)
* Alarmes (CloudWatch Alarms)

---

## 🔁 Fluxo-alvo proposto

1. Cliente envia requisição HTTP
2. API valida e normaliza o payload
3. Evento é publicado na SQS
4. Worker consome a mensagem
5. Processa regras de fraude
6. Persiste no RDS
7. Em caso de falha → DLQ

---

## 📈 Metas propostas de confiabilidade (SRE)

As métricas e metas abaixo são objetivos de projeto. Ainda não representam medições de uma implantação em produção.

### SLIs

* Latência
* Taxa de erro
* Throughput

### SLOs

* Disponibilidade: **99.9%**
* Latência API: **< 200ms**
* Taxa de erro: **< 1%**

### Estratégias

* Retry com backoff exponencial
* Dead Letter Queue (DLQ)
* Idempotência no processamento
* Health checks

---

## ⚙️ Evolução planejada de DevOps

### CI/CD

Pipeline desejado:

```text
- Build
- Test
- Lint
- Security Scan
- Deploy
```

### Infraestrutura

* Terraform (Infrastructure as Code)
* Ambientes isolados:

  * dev
  * staging
  * prod

---

## 💰 FinOps (Custos)

### Principais drivers

* Execução de containers (ECS/Fargate)
* Banco de dados (RDS)
* Mensageria (SQS)
* Logs (CloudWatch)

### Estratégias

* Auto Scaling
* Rightsizing
* Uso sob demanda
* Ambientes efêmeros

---

## 🧪 Modelo de dados-alvo

Tabela principal:

```sql
fraud_alerts (
  id UUID,
  transaction_id VARCHAR,
  user_id VARCHAR,
  risk_score DECIMAL,
  status VARCHAR,
  created_at TIMESTAMP
)
```

---

## 📂 Estrutura atual do repositório

```text
foodtech-fraud-alerts/
├── api-service/
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/br/com/apduartte/api_service/
│       │   └── resources/
│       │       ├── application.yaml
│       │       └── db/migration/
│       │           └── V1__create_alerts_table.sql
│       └── test/
│           └── java/br/com/apduartte/api_service/
├── docs/
│   ├── evidence/
│   │   └── evidence-register.md
│   └── local-development.md
├── .env.example
└── docker-compose.yml
```
