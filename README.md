# 🚀 FoodTech Fraud Alerts

Arquitetura escalável e resiliente para detecção e processamento de alertas de fraude em tempo real na AWS

![AWS](https://img.shields.io/badge/AWS-Cloud-%23FF9900?style=for-the-badge\&logo=amazonaws\&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-%23ED8B00?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/SpringBoot-Framework-%236DB33F?style=for-the-badge\&logo=springboot)
![Terraform](https://img.shields.io/badge/Terraform-IaC-%237B42BC?style=for-the-badge\&logo=terraform)
![Docker](https://img.shields.io/badge/Docker-Container-%230db7ed?style=for-the-badge\&logo=docker)

---

## 📌 Visão Geral

O **FoodTech Fraud Alerts** é uma plataforma orientada a eventos para ingestão, processamento e persistência de alertas de fraude em tempo real.

Projetado com foco em **escalabilidade, resiliência e desacoplamento**, o sistema utiliza mensageria para garantir processamento assíncrono e tolerância a falhas.

---

## 🧠 Princípios Arquiteturais

* **Event-Driven Architecture**
* **Loose Coupling**
* **High Availability**
* **Scalability by Design**
* **Security by Default**
* **Observability First**

---

## 🏗️ Arquitetura

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

## 🔁 Fluxo Detalhado

1. Cliente envia requisição HTTP
2. API valida e normaliza o payload
3. Evento é publicado na SQS
4. Worker consome a mensagem
5. Processa regras de fraude
6. Persiste no RDS
7. Em caso de falha → DLQ

---

## 📈 Confiabilidade (SRE)

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

## ⚙️ DevOps

### CI/CD

Pipeline automatizado com:

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

## 🧪 Modelo de Dados

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

## 📂 Estrutura do Projeto

```text
foodtech-fraud-alerts/
│
├── services/
│   ├── api-service/
│   ├── worker-service/
│
├── infra/
│   ├── modules/
│   ├── environments/
│
├── docs/
│   ├── architecture.md
│   ├── runbook.md
│   ├── decisions.md
│
├── .github/workflows/
│
├── docker/
│
```
