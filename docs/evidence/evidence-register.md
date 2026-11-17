# FoodTech Fraud Alerts
## Engineering Evidence Register

**Document ID:** FFA-EVD-001
**Version:** 1.0
**Date:** 2026-11-16
**Status:** In progress

---

## 1. Purpose

This document maintains the inventory of technical evidence
collected during the development and validation of the
FoodTech Fraud Alerts project.

## 2. Evidence Register

| ID | Evidence | Date | Validation | Status |
|---|---|---|---|---|
| EVD-001 | Docker Desktop environment | 2026-11-11 | Containers observed running | Captured |
| EVD-002 | Spring Boot application.yaml | 2026-11-11 | Configuration inspected | Captured |
| EVD-003 | Docker Compose configuration | 2026-11-11 | Configuration inspected | Captured |
| EVD-004 | Docker Compose validation | 2026-11-11 | docker compose config executed without errors | Validated |
| EVD-005 | PostgreSQL volume inspection | 2026-11-11 | Existing volume mount identified | Validated |
| EVD-006 | Docker volume inventory | 2026-11-11 | Existing volumes listed | Captured |
| EVD-007 | Documentation directory structure | 2026-11-11 | Directory structure verified | Validated |
| EVD-008 | Environment variables protection | 2026-11-12 | .env ignored by Git and absent from tracked files | Validated |
| EVD-009 | Environment example configuration | 2026-11-12 | .env.example inspected; placeholder credentials confirmed | Validated |
| EVD-010 | Docker Compose environment resolution | 2026-11-12 | docker compose config --quiet completed without errors | Validated |
| EVD-011 | PostgreSQL container status | 2026-11-12 | Container observed running on host port 5433 | Validated |
| EVD-012 | Spring Boot database connectivity | 2026-11-12 | Maven test initialized JPA, connected to PostgreSQL 16.15 and completed 1 test with 0 failures | Validated |
| EVD-013 | Flyway migration validation | 2026-11-12 | Flyway validation completed successfully; schema reported at version 1 | Validated |
| EVD-014 | Flyway baseline registration | 2026-11-12 | Schema history contains baseline version 1; no migration execution established by this record | Validated |
| EVD-015 | Existing alerts table inspection | 2026-11-12 | Existing alerts table identified in public schema; table existence does not establish Flyway creation | Validated |
| EVD-016 | Local development guide review | 2026-11-12 | Flyway, Git Bash, database connectivity and shutdown instructions reviewed | Validated |
| EVD-017 | Spring Boot Actuator health endpoint | 2026-11-12 | HTTP GET returned 200 OK and `{"status":"UP"}` on localhost:8080 | Validated |
| EVD-018 | PostgreSQL alerts schema validation | 2026-11-13 | Five columns, NOT NULL constraints, UUID primary key, VARCHAR limits of 20 and 60, and status CHECK constraint verified | Validated |
| EVD-019 | Flyway migration on empty database | 2026-11-13 | Fresh database initialized successfully; V1 created alerts table and flyway_schema_history; version 1 recorded with success = true | Validated |
| EVD-020 | Maven clean test after environment variables were loaded | 2026-11-14 | Spring context initialized, PostgreSQL 16.15 connection established, Flyway migration history validated, 1 test passed with 0 failures | Validated |
| EVD-021 | AlertRepositoryTests integration test | 2026-11-14 | Alert insertion and retrieval in PostgreSQL 16.15 using saveAndFlush(), JPA persistence context clearing and lookup by ID; 1 test passed, 0 failures, 0 errors | Validated |
| EVD-022 | Maven test with current Flyway and Hibernate configuration | 2026-11-16 | Spring Boot 3.5.9, Java 21.0.13 and PostgreSQL 16.15; Flyway history validated; Hibernate initialized with ddl-auto=validate; 2 tests passed, 0 failures, 0 errors, 0 skipped | Validated |
| EVD-023 | Docker Compose PostgreSQL health validation | 2026-11-16 | Compose configuration validated; PostgreSQL container running on host port 5433; Docker health status confirmed as healthy | Validated |
| EVD-024 | API request and response DTOs with Jakarta Bean Validation | 2026-11-16 | AlertRequest and AlertResponse implemented; AlertRequest validation tests executed; Maven clean test completed with 6 tests, 0 failures, 0 errors and 0 skipped | Validated |


### 2.1 Flyway Baseline and Migration Traceability

The primary database (`foodtech`) already contained the `public.alerts` table when Flyway was introduced.

During the initial Flyway adoption, the configuration `baseline-on-migrate: true` was temporarily enabled to register baseline version 1 for the existing schema.

This setting was subsequently removed from the current `application.yaml` configuration. Automatic baseline registration is therefore not enabled in the current project configuration.

The primary database history records:

- Version: `1`
- Description: `<< Flyway Baseline >>`
- Success: `true`

**Important:** The baseline registration does not mean that `V1__create_alerts_table.sql` was executed against the primary database. It records the existing schema as the starting point for Flyway version control.

The existing `public.alerts` table was independently inspected. Its columns, data types, primary key and status check constraint were found compatible with the V1 migration definition.

Migration execution was independently verified against the fresh database `foodtech_migration_check`. In this database, Flyway successfully executed `V1__create_alerts_table.sql` and recorded:

- Version: `1`
- Description: `create alerts table`
- Success: `true`

These results establish two distinct validation paths:

1. **Existing database adoption:** baseline registration and schema compatibility verification.
2. **Fresh database initialization:** successful execution of the versioned V1 migration.

This distinction preserves migration traceability and prevents baseline registration from being misrepresented as migration execution.

## 3. Evidence Classification

- Captured: evidence recorded but not necessarily independently validated.
- Validated: command or technical behavior successfully verified.
- Pending: evidence still needs to be collected.
- Planned: future implementation or validation.

## 4. Evidence Management Rules

1. Every relevant technical validation must have a unique ID.
2. Evidence must include date, context and expected result.
3. Screenshots must not expose credentials or secrets.
4. Planned capabilities must not be represented as implemented.
5. Evidence files must be stored in the corresponding directory.
6. Technical claims must be traceable to code, configuration or test results.

## 5. Pending Evidence

- CRUD operations validation for the alerts resource.
- Final Git diff review.
- First commit verification.
---

**Project:** FoodTech Fraud Alerts
**Repository:** https://github.com/apduartte/foodtech-fraud-alerts
