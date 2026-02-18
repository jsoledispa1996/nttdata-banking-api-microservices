# 🏦 NTT DATA - Banking Microservices API

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-Reactive-blue.svg)](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-7.6.0-black.svg)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema de microservicios reactivos para gestión bancaria construido con **Spring Boot 3.2**, **Spring WebFlux**, **R2DBC**, **Kafka** y una arquitectura hexagonal limpia.

---

## 📋 Tabla de Contenidos

- [Características Principales](#-características-principales)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Ejecución del Proyecto](#-ejecución-del-proyecto)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [API Endpoints](#-api-endpoints)
- [Testing](#-testing)
- [Base de Datos](#-base-de-datos)
- [Troubleshooting](#-troubleshooting)
- [Roadmap](#-roadmap)
- [Autor](#-autor)

---

## ✨ Características Principales

### 🎯 Funcionalidades Core

- ✅ **F1**: Gestión completa de clientes (CRUD con validaciones)
- ✅ **F2**: Gestión de cuentas bancarias (Ahorro/Corriente)
- ✅ **F3**: Registro de movimientos (Crédito/Débito con validación de saldo)
- ✅ **F4**: Reportes de estado de cuenta por fecha y cliente
- ✅ **F5**: Suite completa de pruebas unitarias (JUnit 5 + Mockito)
- ✅ **F6**: Pruebas de integración (Testcontainers con MySQL + Kafka)

### 🚀 Características Técnicas Avanzadas

#### Arquitectura y Diseño
- **Arquitectura Hexagonal** (Puertos y Adaptadores)
- **Domain-Driven Design (DDD)** con capas bien definidas
- **Programación Reactiva** con Project Reactor
- **Comunicación Asíncrona** mediante Apache Kafka
- **Base de datos reactiva** con R2DBC + MySQL

#### Seguridad
- **JWT Authentication** (JSON Web Tokens)
- **Spring Security** con WebFlux Security
- **Password Encryption** con BCrypt
- **Validación de entrada** con Jakarta Bean Validation

#### Testing
- **Unit Tests** con JUnit 5 + Mockito + AssertJ
- **Integration Tests** con Testcontainers (MySQL + Kafka)
- **StepVerifier** para testing reactivo
- **WebTestClient** para testing de endpoints HTTP
- **Cobertura**: 5 unit tests + 3 integration tests

#### DevOps y Calidad
- **Docker Compose** para orquestación multi-contenedor
- **Healthchecks** en todos los servicios
- **Logs estructurados** con SLF4J + Logback
- **Clean Architecture** con separación de responsabilidades
- **MapStruct** para mapeo de DTOs
- **Lombok** para reducción de boilerplate

---

## 🏗️ Arquitectura del Sistema

### Diagrama de Microservicios

```
┌─────────────────────────────────────────────────────────────────┐
│                        EXTERNAL CLIENT                          │
│                     (Postman / Frontend)                        │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
        ┌────────────────────────────────────────────────┐
        │         Auth Service (Port 8080)               │
        │   ┌──────────────────────────────────────┐     │
        │   │  - JWT Token Generation              │     │
        │   │  - Token Validation                  │     │
        │   │  - User Authentication               │     │
        │   └──────────────────────────────────────┘     │
        └────────────────────────────────────────────────┘
                             │
                    ┌────────┴─────────┐
                    │                  │
                    ▼                  ▼
       ┌────────────────────┐  ┌────────────────────┐
       │  Customer Service  │  │  Account Service   │
       │   (Port 8081)      │  │   (Port 8082)      │
       │                    │  │                    │
       │  - Gestión de      │  │  - Gestión de      │
       │    Clientes        │  │    Cuentas         │
       │  - Spring WebFlux  │  │  - Gestión de      │
       │  - R2DBC           │  │    Movimientos     │
       │  - Kafka Producer  │  │  - Reportes        │
       │                    │  │  - Kafka Consumer  │
       └─────────┬──────────┘  └──────────┬─────────┘
                 │                        │
                 ▼                        ▼
       ┌─────────────────┐      ┌─────────────────┐
       │  MySQL (3306)   │      │  MySQL (3306)   │
       │  - customerdb   │      │  - accountdb    │
       └─────────────────┘      └─────────────────┘
                 │                        │
                 └────────┬───────────────┘
                          │
                          ▼
              ┌───────────────────────┐
              │ Apache Kafka (9092)   │
              │  - customer-events    │
              │  - Zookeeper (2181)   │
              └───────────────────────┘
```

### Capas de la Arquitectura Hexagonal

```
┌──────────────────────────────────────────────────────────┐
│                   INFRASTRUCTURE LAYER                   │
│  ┌────────────────────────────────────────────────────┐  │
│  │   REST Controllers │ Kafka Consumers │ Security   │  │
│  │   - MovementController                            │  │
│  │   - AccountController                             │  │
│  │   - CustomerEventConsumer                         │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│                   APPLICATION LAYER                      │
│  ┌────────────────────────────────────────────────────┐  │
│  │   Services (Business Logic)                       │  │
│  │   - MovementService                               │  │
│  │   - AccountService                                │  │
│  │   - CustomerService                               │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│                     DOMAIN LAYER                         │
│  ┌────────────────────────────────────────────────────┐  │
│  │   Entities │ Value Objects │ Domain Events        │  │
│  │   - Movement (Aggregate)                          │  │
│  │   - Account (Entity)                              │  │
│  │   - Customer (Entity)                             │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│                   INFRASTRUCTURE LAYER                   │
│  ┌────────────────────────────────────────────────────┐  │
│  │   Persistence │ External Services │ Messaging     │  │
│  │   - R2DBC Repositories                            │  │
│  │   - Kafka Producers                               │  │
│  │   - Mappers (MapStruct)                           │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologías Utilizadas

### Backend Framework
- **Java 17** - LTS version
- **Spring Boot 3.2.2** - Framework principal
- **Spring WebFlux** - Programación reactiva
- **Spring Data R2DBC** - Base de datos reactiva
- **Spring Security 6.2.1** - Autenticación y autorización
- **Spring Kafka 3.1.1** - Mensajería asíncrona

### Base de Datos
- **MySQL 8.0** - Base de datos relacional
- **R2DBC MySQL 1.1.2** - Driver reactivo
- **Flyway/Liquibase** - Migraciones de BD (por implementar)

### Mensajería
- **Apache Kafka 7.6.0** - Event streaming
- **Confluent Platform** - Zookeeper + Kafka

### Seguridad
- **JWT (JSON Web Tokens)** - JJWT 0.12.5
- **BCrypt** - Encriptación de contraseñas
- **OAuth2 Resource Server** - Spring Security OAuth2

### Testing
- **JUnit 5.10.1** - Framework de testing
- **Mockito 5.7.0** - Mocking framework
- **AssertJ 3.24.2** - Assertions fluidas
- **Testcontainers 1.19.3** - Containers para tests
- **Reactor Test 3.6.2** - Testing reactivo (StepVerifier)
- **WebTestClient** - Testing HTTP reactivo

### Utilidades
- **Lombok 1.18.30** - Reducción de boilerplate
- **MapStruct 1.5.5.Final** - Mapeo de objetos
- **SLF4J + Logback** - Logging
- **Jakarta Validation** - Validación de beans

### DevOps
- **Docker & Docker Compose** - Contenerización
- **Maven 3.9+** - Build tool
- **Git** - Control de versiones

---

## 📦 Requisitos Previos

### Software Requerido

| Software       | Versión Mínima | Recomendada | Descarga                                    |
|----------------|----------------|-------------|---------------------------------------------|
| Java JDK       | 17             | 17 LTS      | [OpenJDK](https://adoptium.net/)            |
| Docker Desktop | 20.10+         | 28.5+       | [Docker](https://www.docker.com/)           |
| Maven          | 3.8+           | 3.9+        | [Maven](https://maven.apache.org/)          |
| Git            | 2.30+          | 2.40+       | [Git](https://git-scm.com/)                 |
| Postman        | 10.0+          | Latest      | [Postman](https://www.postman.com/)         |

### Opcional (para desarrollo local sin Docker)
- **MySQL 8.0+**
- **Apache Kafka 3.6+**
- **Zookeeper 3.8+**

---

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd ntt-data-banking-api-microservices
```

### 2. Configurar Variables de Entorno

Crear archivo `.env` en la raíz del proyecto:

```bash
# Copiar archivo de ejemplo
cp .env.example .env
```

Contenido del archivo `.env`:

```bash
# MySQL Configuration
MYSQL_ROOT_PASSWORD=rootpass
MYSQL_DATABASE=accountdb
MYSQL_USER=banking_user
MYSQL_PASSWORD=banking_pass

# JWT Configuration
JWT_SECRET=nttdata-banking-microservices-secret-key-2024-super-secure-min-256-bits-hmac-sha256
JWT_EXPIRATION=300  # 5 minutos en segundos

# Application Profiles
SPRING_PROFILES_ACTIVE=prod
```

### 3. Construir el Proyecto

```bash
# Compilar todos los módulos
mvn clean install -DskipTests

# O compilar con tests
mvn clean install
```

---

## 🎯 Ejecución del Proyecto

### Opción 1: Docker Compose (RECOMENDADO)

**Levantar todos los servicios:**

```bash
# Construir imágenes y levantar contenedores
docker-compose up -d --build

# Ver logs en tiempo real
docker-compose logs -f

# Ver estado de servicios
docker-compose ps
```

**Servicios levantados:**
- ✅ **MySQL** - `localhost:3306`
- ✅ **Zookeeper** - `localhost:2181`
- ✅ **Kafka** - `localhost:9092` (interno), `localhost:29092` (externo)
- ✅ **Auth Service** - `localhost:8080`
- ✅ **Customer Service** - `localhost:8081`
- ✅ **Account Service** - `localhost:8082`

**Verificar que todo funciona:**

```bash
# Health checks
curl http://localhost:8080/actuator/health  # Auth Service
curl http://localhost:8081/actuator/health  # Customer Service
curl http://localhost:8082/actuator/health  # Account Service
```

**Detener servicios:**

```bash
# Detener contenedores
docker-compose down

# Detener y eliminar volúmenes (limpieza completa)
docker-compose down -v
```

### Opción 2: Ejecución Local (Sin Docker)

**Requisitos:**
- MySQL 8.0 corriendo en `localhost:3306`
- Kafka corriendo en `localhost:9092`

**Pasos:**

1. **Crear bases de datos:**

```sql
CREATE DATABASE customerdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE accountdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Importar scripts
SOURCE BaseDatos/customerdb.sql;
SOURCE BaseDatos/accountdb.sql;
```

2. **Levantar servicios en orden:**

```bash
# Terminal 1 - Auth Service
cd nttdata-banking-microservices/nttdata-auth-service
mvn spring-boot:run

# Terminal 2 - Customer Service
cd nttdata-banking-microservices/nttdata-customer-service
mvn spring-boot:run

# Terminal 3 - Account Service
cd nttdata-banking-microservices/nttdata-account-service
mvn spring-boot:run
```

---

## 📁 Estructura del Proyecto

```
ntt-data-banking-api-microservices/
│
├── .env                          # Variables de entorno
├── .env.example                  # Ejemplo de variables
├── docker-compose.yml            # Orquestación de servicios
├── README.md                     # Este archivo
├── postman/                      # Colección Postman
│   ├── README.md
│   └── NTT DATA - Banking Microservices.postman_collection.json
│
├── BaseDatos/                    # Scripts SQL
│   ├── customerdb.sql
│   └── accountdb.sql
│
└── nttdata-banking-microservices/
    ├── pom.xml                   # POM padre
    │
    ├── nttdata-shared-library/   # Librería compartida
    │   └── src/main/java/com/nttdata/banking/shared/
    │       ├── events/           # Domain Events (Kafka)
    │       │   ├── BaseEvent.java
    │       │   ├── CustomerCreatedEvent.java
    │       │   ├── CustomerUpdatedEvent.java
    │       │   └── CustomerDeletedEvent.java
    │       ├── exceptions/       # Excepciones de negocio
    │       │   ├── BusinessException.java
    │       │   ├── InsufficientBalanceException.java
    │       │   └── ResourceNotFoundException.java
    │       ├── security/         # Componentes de seguridad
    │       │   ├── JwtAuthenticationFilter.java
    │       │   └── JwtTokenProvider.java
    │       └── utils/            # Utilidades
    │           └── CorrelationIdUtil.java
    │
    ├── nttdata-auth-service/     # Servicio de Autenticación
    │   ├── Dockerfile
    │   └── src/main/java/com/nttdata/banking/auth/
    │       ├── AuthServiceApplication.java
    │       ├── config/           # Configuración
    │       │   └── SecurityConfig.java
    │       ├── controller/       # REST Controllers
    │       │   └── AuthController.java
    │       └── service/          # Lógica de negocio
    │           └── AuthService.java
    │
    ├── nttdata-customer-service/ # Servicio de Clientes (F1)
    │   ├── Dockerfile
    │   └── src/
    │       ├── main/java/com/nttdata/banking/customer/
    │       │   ├── CustomerServiceApplication.java
    │       │   ├── domain/       # Entidades de dominio
    │       │   │   ├── model/
    │       │   │   │   ├── Customer.java
    │       │   │   │   └── Person.java
    │       │   │   └── repository/
    │       │   │       └── CustomerRepository.java
    │       │   ├── application/  # Casos de uso
    │       │   │   ├── service/
    │       │   │   │   └── CustomerService.java
    │       │   │   └── mapper/
    │       │   │       └── CustomerMapper.java
    │       │   └── infrastructure/ # Adaptadores
    │       │       ├── rest/
    │       │       │   ├── CustomerController.java
    │       │       │   └── dto/
    │       │       │       ├── CustomerRequest.java
    │       │       │       └── CustomerResponse.java
    │       │       ├── messaging/
    │       │       │   └── CustomerEventPublisher.java
    │       │       └── config/
    │       │           ├── KafkaConfig.java
    │       │           ├── R2dbcConfig.java
    │       │           └── SecurityConfig.java
    │       └── test/             # Tests
    │           └── java/com/nttdata/banking/customer/
    │
    └── nttdata-account-service/  # Servicio de Cuentas (F2, F3, F4)
        ├── Dockerfile
        └── src/
            ├── main/java/com/nttdata/banking/account/
            │   ├── AccountServiceApplication.java
            │   ├── domain/       # Dominio
            │   │   ├── model/
            │   │   │   ├── Account.java
            │   │   │   ├── Movement.java
            │   │   │   ├── AccountType.java (enum)
            │   │   │   └── MovementType.java (enum)
            │   │   └── repository/
            │   │       ├── AccountRepository.java
            │   │       └── MovementRepository.java
            │   ├── application/  # Aplicación
            │   │   ├── service/
            │   │   │   ├── AccountService.java
            │   │   │   └── MovementService.java (F2, F3)
            │   │   └── mapper/
            │   │       ├── AccountMapper.java
            │   │       └── MovementMapper.java
            │   └── infrastructure/ # Infraestructura
            │       ├── rest/
            │       │   ├── MovementController.java
            │       │   ├── AccountController.java
            │       │   ├── ReportController.java (F4)
            │       │   ├── dto/
            │       │   │   ├── CreateMovementRequest.java
            │       │   │   ├── MovementResponse.java
            │       │   │   └── AccountReportResponse.java
            │       │   └── exception/
            │       │       └── GlobalExceptionHandler.java
            │       ├── messaging/
            │       │   └── CustomerEventConsumer.java
            │       └── config/
            │           ├── KafkaConfig.java
            │           ├── R2dbcConfig.java
            │           └── SecurityConfig.java
            │
            └── test/              # Tests (F5, F6)
                ├── java/com/nttdata/banking/account/
                │   ├── service/
                │   │   └── MovementServiceTest.java      # F5: Unit Tests
                │   └── integration/
                │       ├── MovementIntegrationTest.java  # F6: Integration Tests
                │       └── config/
                │           └── TestSecurityConfig.java
                └── resources/
                    ├── schema.sql                        # Test DB Schema
                    └── application-test.properties       # Test Configuration
```

---

## 🌐 API Endpoints

### 🔐 Auth Service (Port 8080)

| Método | Endpoint           | Descripción              | Body                         | Autenticación |
|--------|--------------------|--------------------------|------------------------------|---------------|
| POST   | `/api/auth/login`  | Obtener token JWT        | `{username, password}`       | ❌ No         |
| GET    | `/api/auth/validate` | Validar token          | -                            | ✅ JWT        |

### 👤 Customer Service (Port 8081)

| Método | Endpoint                   | Descripción                  | Autenticación |
|--------|----------------------------|------------------------------|---------------|
| GET    | `/api/v1/customers`        | Listar clientes              | ✅ JWT        |
| GET    | `/api/v1/customers/{id}`   | Obtener cliente por ID       | ✅ JWT        |
| POST   | `/api/v1/customers`        | Crear cliente (F1)           | ✅ JWT        |
| PUT    | `/api/v1/customers/{id}`   | Actualizar cliente           | ✅ JWT        |
| DELETE | `/api/v1/customers/{id}`   | Eliminar cliente (lógico)    | ✅ JWT        |

### 💰 Account Service (Port 8082)

#### Cuentas (F2)

| Método | Endpoint                          | Descripción                 | Autenticación |
|--------|-----------------------------------|-----------------------------|---------------|
| GET    | `/api/v1/accounts`                | Listar cuentas              | ✅ JWT        |
| GET    | `/api/v1/accounts/{id}`           | Obtener cuenta por ID       | ✅ JWT        |
| GET    | `/api/v1/accounts/number/{number}` | Obtener por número cuenta  | ✅ JWT        |
| POST   | `/api/v1/accounts`                | Crear cuenta (F2)           | ✅ JWT        |
| PUT    | `/api/v1/accounts/{id}`           | Actualizar cuenta           | ✅ JWT        |
| DELETE | `/api/v1/accounts/{id}`           | Eliminar cuenta             | ✅ JWT        |

#### Movimientos (F2, F3)

| Método | Endpoint                              | Descripción                     | Autenticación |
|--------|---------------------------------------|---------------------------------|---------------|
| GET    | `/api/v1/movements`                   | Listar movimientos              | ✅ JWT        |
| GET    | `/api/v1/movements/{id}`              | Obtener movimiento por ID       | ✅ JWT        |
| GET    | `/api/v1/movements/account/{id}`      | Movimientos por cuenta          | ✅ JWT        |
| GET    | `/api/v1/movements/customer/{code}`   | Movimientos por cliente         | ✅ JWT        |
| POST   | `/api/v1/movements`                   | Crear movimiento (F2, F3)       | ✅ JWT        |

**Request Body - Crear Movimiento:**
```json
{
  "accountNumber": "478758",
  "movementType": "DEPOSITO",  // o "RETIRO"
  "amount": 600.00,
  "description": "Depósito en efectivo"
}
```

**Validaciones F3:**
- ✅ **Crédito (DEPOSITO)**: Suma al saldo actual
- ✅ **Débito (RETIRO)**: Resta del saldo actual
- ❌ **Si saldo insuficiente**: Retorna 400 BAD REQUEST con mensaje "Saldo no disponible"

#### Reportes (F4)

| Método | Endpoint                                      | Descripción                  | Autenticación |
|--------|-----------------------------------------------|------------------------------|---------------|
| GET    | `/api/v1/reports/customer/{code}?from&to`     | Reporte por cliente y fechas | ✅ JWT        |
| GET    | `/api/v1/reports/account/{number}?from&to`    | Reporte por cuenta y fechas  | ✅ JWT        |

**Query Parameters:**
- `from` (opcional): Fecha inicio (ISO-8601: `2025-01-01T00:00:00`)
- `to` (opcional): Fecha fin (ISO-8601: `2025-12-31T23:59:59`)

**Response Example:**
```json
{
  "customerCode": "CLI100",
  "customerName": "Jose Lema",
  "accounts": [
    {
      "accountNumber": "478758",
      "accountType": "AHORRO",
      "initialBalance": 2000.00,
      "currentBalance": 2600.00,
      "movements": [
        {
          "date": "2025-02-18T07:05:31",
          "movementType": "DEPOSITO",
          "amount": 600.00,
          "balance": 2600.00,
          "description": "Depósito en efectivo"
        }
      ]
    }
  ]
}
```

---

## 🧪 Testing

### Cobertura de Tests

El proyecto incluye **8 tests** que cubren los requisitos F5 y F6:

#### F5: Unit Tests (5 tests)
- ✅ `testCreateMovement_Deposit_Success`
- ✅ `testCreateMovement_InsufficientBalance_ThrowsException`
- ✅ `testGetMovementById_Success`
- ✅ `testGetMovementById_NotFound_ThrowsException`
- ✅ `testCreateMovement_InvalidAmount_ThrowsException`

#### F6: Integration Tests (3 tests)
- ✅ `testCreateMovement_Deposit_CompleteFlow`
- ✅ `testCreateMovement_InsufficientBalance_ReturnsError`
- ✅ `testMultipleMovements_BalanceUpdatesCorrectly`

### Ejecutar Tests

#### Todos los Tests

```bash
# Desde la raíz del proyecto
mvn clean test

# Solo Account Service
cd nttdata-banking-microservices/nttdata-account-service
mvn test
```

#### Tests Unitarios (Rápidos)

```bash
mvn test -Dtest=MovementServiceTest
```

#### Tests de Integración (Requieren Docker)

```bash
# Asegurarse de que Docker esté corriendo
mvn test -Dtest=MovementIntegrationTest
```

**Nota**: Los tests de integración usan **Testcontainers**, que levanta automáticamente:
- MySQL 8.0 (contenedor temporal)
- Kafka 7.5.0 (contenedor temporal)

### Ver Resultados

```bash
# Ver reporte de cobertura
mvn surefire-report:report
open target/site/surefire-report.html
```

### Características del Testing

#### Unit Tests
- **Framework**: JUnit 5 + Mockito
- **Assertions**: AssertJ
- **Reactive Testing**: StepVerifier
- **Mocking**: `@Mock`, `@InjectMocks`
- **Velocidad**: < 1 segundo por test

#### Integration Tests
- **Framework**: Testcontainers + JUnit 5
- **HTTP Client**: WebTestClient
- **Containers**: MySQL + Kafka (auto-provisioned)
- **DB Initialization**: `schema.sql` con `withInitScript()`
- **Security**: Deshabilitada con `TestSecurityConfig`
- **Cleanup**: `@BeforeEach` / `@AfterEach` con try-catch
- **Duración**: ~30 segundos (incluye startup de containers)

---

## 💾 Base de Datos

### Esquema - Customer Database

**Tabla: `cl_clientes`**

```sql
CREATE TABLE cl_clientes (
    cl_id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    cl_codigo_cliente VARCHAR(20) NOT NULL UNIQUE,
    pe_nombre VARCHAR(100) NOT NULL,
    pe_genero ENUM('M', 'F', 'OTRO') NOT NULL,
    pe_edad INT NOT NULL,
    pe_identificacion VARCHAR(20) NOT NULL UNIQUE,
    pe_direccion VARCHAR(255),
    pe_telefono VARCHAR(20),
    cl_contrasena VARCHAR(255) NOT NULL,
    cl_estado BOOLEAN DEFAULT TRUE,
    cl_fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cl_fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Esquema - Account Database

**Tabla: `ba_cuentas`**

```sql
CREATE TABLE ba_cuentas (
    cu_id_cuenta BIGINT AUTO_INCREMENT PRIMARY KEY,
    cu_numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    cu_tipo_cuenta ENUM('AHORRO', 'CORRIENTE') NOT NULL,
    cu_saldo_inicial DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    cu_saldo_actual DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    cu_estado BOOLEAN DEFAULT TRUE,
    cu_id_cliente VARCHAR(20) NOT NULL,
    cu_fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cu_fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cliente (cu_id_cliente),
    INDEX idx_numero (cu_numero_cuenta)
);
```

**Tabla: `ba_movimientos`**

```sql
CREATE TABLE ba_movimientos (
    mo_id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    mo_fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    mo_tipo_movimiento ENUM('DEPOSITO', 'RETIRO') NOT NULL,
    mo_valor DECIMAL(15,2) NOT NULL,
    mo_saldo DECIMAL(15,2) NOT NULL,
    mo_descripcion VARCHAR(255),
    mo_id_cuenta BIGINT NOT NULL,
    FOREIGN KEY (mo_id_cuenta) REFERENCES ba_cuentas(cu_id_cuenta) ON DELETE CASCADE,
    INDEX idx_cuenta (mo_id_cuenta),
    INDEX idx_fecha (mo_fecha)
);
```

### Datos de Prueba

Los scripts SQL incluyen datos de ejemplo:

**Clientes:**
- `CLI100` - Jose Lema
- `CLI200` - Marianela Montalvo
- `CLI300` - Juan Osorio

**Cuentas:**
- `478758` - Ahorro - $2000
- `225487` - Corriente - $100
- `495878` - Ahorro - $0
- `496825` - Ahorro - $540

---

## 🔧 Troubleshooting

### Problema 1: Puerto ocupado

**Error:**
```
Error starting userland proxy: listen tcp4 0.0.0.0:3306: bind: address already in use
```

**Solución:**
```bash
# Verificar qué usa el puerto
netstat -ano | findstr :3306    # Windows
lsof -i :3306                   # Linux/Mac

# Detener MySQL local o cambiar puerto en docker-compose.yml
# Option 1: Detener MySQL local
sudo systemctl stop mysql       # Linux
brew services stop mysql        # Mac

# Option 2: Cambiar puerto en docker-compose.yml
# mysql:
#   ports:
#     - "3307:3306"  # Exponer en puerto diferente
```

### Problema 2: Containers no inician

**Error:**
```
ERROR: Container mysql is unhealthy
```

**Solución:**
```bash
# Ver logs detallados
docker-compose logs mysql

# Limpiar volúmenes y reiniciar
docker-compose down -v
docker-compose up -d --build

# Esperar 60 segundos para inicialización completa
```

### Problema 3: Tests de integración fallan

**Error:**
```
Could not find a valid Docker environment
```

**Solución:**
```bash
# Verificar que Docker Desktop esté corriendo
docker ps

# Verificar variable de entorno (si usas Docker remoto)
echo $DOCKER_HOST

# Reinstalar Testcontainers
mvn clean install -U
```

### Problema 4: Error 401 Unauthorized

**Causa:** Token JWT no enviado o expirado

**Solución:**
```bash
# 1. Obtener nuevo token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# 2. Usar token en requests
curl http://localhost:8082/api/v1/movements \
  -H "Authorization: Bearer <TOKEN>"
```

### Problema 5: Kafka no conecta

**Error:**
```
Connection to node -1 could not be established
```

**Solución:**
```bash
# Verificar que Zookeeper y Kafka estén UP
docker-compose ps

# Reiniciar solo Kafka
docker-compose restart kafka

# Ver logs de Kafka
docker-compose logs -f kafka
```

---

## 🗺️ Roadmap

### ✅ Completado

- [x] Arquitectura hexagonal con DDD
- [x] Programación reactiva (Spring WebFlux + R2DBC)
- [x] Comunicación asíncrona (Kafka)
- [x] Autenticación JWT
- [x] CRUD completo de clientes (F1)
- [x] Gestión de cuentas (F2)
- [x] Registro de movimientos con validación de saldo (F3)
- [x] Reportes de estado de cuenta (F4)
- [x] Suite de tests unitarios (F5)
- [x] Tests de integración con Testcontainers (F6)
- [x] Docker Compose para orquestación
- [x] Colección Postman completa

### 🚧 En Proceso

- [ ] OpenAPI/Swagger Documentation (Contract First)
- [ ] Traducción de código a inglés
- [ ] Métricas con Micrometer + Prometheus
- [ ] Distributed Tracing con Zipkin

### 📋 Backlog

#### Performance & Scalability
- [ ] Cache distribuido con Redis
- [ ] Circuit Breaker con Resilience4j
- [ ] Rate Limiting con Bucket4j

#### Seguridad
- [ ] OAuth2 completo (Authorization Code Flow)
- [ ] Refresh Tokens
- [ ] Auditoría de acciones

#### DevOps
- [ ] CI/CD con GitHub Actions
- [ ] Kubernetes manifests (Helm charts)
- [ ] Health checks avanzados (Liveness/Readiness)

#### Calidad
- [ ] Mutation Testing con PIT
- [ ] Coverage > 80% con JaCoCo
- [ ] SonarQube analysis
- [ ] Código completo en inglés

---

## 📚 Documentación Adicional

- **[Postman Guide](./postman/README.md)** - Guía completa de uso de la colección Postman
- **[API Examples](./postman/)** - Ejemplos de requests/responses
- **[Architecture Decision Records](./docs/adr/)** - Decisiones de arquitectura (por crear)
- **[Contributing Guide](./CONTRIBUTING.md)** - Guía de contribución (por crear)

---

## 👨‍💻 Autor

**José Soledispa**

- 💼 LinkedIn: [Perfil](https://www.linkedin.com/in/jsoledispa/)
- 🐱 GitHub: [jsoledispa](https://github.com/jsoledispa)
- 📧 Email: jose.soledispa@outlook.com

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

---

## 🙏 Agradecimientos

- **NTT DATA** - Por la oportunidad de desarrollar este proyecto
- **Spring Team** - Por el excelente framework reactivo
- **Testcontainers** - Por facilitar los tests de integración
- **Comunidad Open Source** - Por las librerías y herramientas utilizadas

---

## 📞 Soporte

Si encuentras algún problema o tienes preguntas:

1. 📖 Revisa la sección [Troubleshooting](#-troubleshooting)
2. 📋 Consulta la [documentación de Postman](./postman/README.md)
3. 🐛 Abre un issue en GitHub (si aplica)
4. 📧 Contacta al autor

---

<div align="center">

**⭐ Si te resultó útil este proyecto, considera darle una estrella ⭐**

Hecho con ❤️ usando Spring Boot 3.2 + WebFlux + Kafka

</div>
