# 🧪 Guía de Pruebas - Banking API Microservices con Postman

**Versión**: 1.0.0  
**Última actualización**: 18 de Febrero 2026

---

## 📋 Tabla de Contenidos

1. [Introducción](#-introducción)
2. [Requisitos Previos](#-requisitos-previos)
3. [Importar Colección](#-importar-colección)
4. [Configurar Variables](#-configurar-variables)
5. [Flujo de Pruebas Completo](#-flujo-de-pruebas-completo)
6. [Casos de Prueba por Módulo](#-casos-de-prueba-por-módulo)
7. [Troubleshooting](#-troubleshooting)
8. [Ejemplos de Respuestas](#-ejemplos-de-respuestas)

---

## 📖 Introducción

Esta guía proporciona instrucciones detalladas para probar la **Banking API Microservices** usando **Postman**. La colección incluye todos los endpoints necesarios para validar los requisitos funcionales **F1 a F6**.

### Características de la Colección

- ✅ **35 requests** organizados por servicio
- ✅ **Autenticación JWT** preconfigurada
- ✅ **Variables de entorno** para fácil configuración
- ✅ **Ejemplos de respuestas** para casos exitosos y errores
- ✅ **Tests automáticos** en cada endpoint
- ✅ **Documentación detallada** con ejemplos

### Arquitectura del Sistema

```
┌─────────────────────────────────────────────┐
│         POSTMAN (Cliente HTTP)              │
└────────────────┬────────────────────────────┘
                 │
                 ▼
        ┌─────────────────┐
        │  Auth Service   │  ← Login/Token
        │   Port 8080     │
        └─────────────────┘
                 │
        ┌────────┴──────────┐
        ▼                   ▼
┌──────────────┐    ┌──────────────┐
│  Customer    │    │   Account    │
│  Service     │    │   Service    │
│  Port 8081   │    │   Port 8082  │
└──────────────┘    └──────────────┘
       │                    │
       └────────┬───────────┘
                ▼
        ┌──────────────┐
        │    MySQL     │
        │   Port 3306  │
        └──────────────┘
```

---

## ✅ Requisitos Previos

### 1. Software Instalado

| Software       | Versión Mínima | Descarga                                    |
|----------------|----------------|---------------------------------------------|
| **Docker Desktop** | 20.10+     | [Docker](https://www.docker.com/)           |
| **Postman**    | 10.0+          | [Postman](https://www.postman.com/)         |

### 2. Servicios Levantados

**Levantar todos los servicios con Docker Compose:**

```bash
# Desde la raíz del proyecto
cd ntt-data-banking-api-microservices

# Levantar servicios
docker-compose up -d --build

# Verificar que todo esté UP
docker-compose ps
```

**Expected output:**
```
NAME                        STATUS          PORTS
kafka                       Up              0.0.0.0:9092->9092/tcp
mysql-nttdata-banking       Up (healthy)    0.0.0.0:3306->3306/tcp
nttdata-account-service     Up              0.0.0.0:8082->8082/tcp
nttdata-auth-service        Up (healthy)    0.0.0.0:8080->8080/tcp
nttdata-customer-service    Up              0.0.0.0:8081->8081/tcp
zookeeper                   Up              0.0.0.0:2181->2181/tcp
```

### 3. Verificar Conectividad

```bash
# Health checks
curl http://localhost:8080/actuator/health  # Auth Service
curl http://localhost:8081/actuator/health  # Customer Service
curl http://localhost:8082/actuator/health  # Account Service

# Expected: {"status":"UP"}
```

---

## 📥 Importar Colección

### Opción 1: Importar desde Archivo (RECOMENDADO)

1. **Abrir Postman**
2. **Click en "Import"** (esquina superior izquierda)
3. **Seleccionar "File"**
4. **Navegar a:**
   ```
   ntt-data-banking-api-microservices/postman/
   NTT DATA - Banking Microservices.postman_collection.json
   ```
5. **Click "Import"**
6. ✅ Verás la colección "**NTT DATA - Banking Microservices**"

### Opción 2: Drag & Drop

1. Abre Postman
2. Arrastra el archivo `.json` a la ventana de Postman
3. Confirma la importación

### Verificación

La colección importada debe contener:

```
📁 NTT DATA - Banking Microservices
│
├── 📁 Auth Service (2 requests)
│   ├── POST Login
│   └── GET Validate Token
│
├── 📁 Customer Service (7 requests)
│   ├── GET List Customers
│   ├── GET Customer by ID
│   ├── POST Create Customer
│   ├── PUT Update Customer
│   ├── DELETE Delete Customer (Lógico)
│   ├── DELETE Delete Customer (Físico)
│   └── GET Health Check
│
├── 📁 Account Service - Accounts (7 requests)
│   ├── GET List Accounts
│   ├── GET Account by ID
│   ├── GET Account by Number
│   ├── POST Create Account
│   ├── PUT Update Account
│   ├── DELETE Delete Account
│   └── GET Health Check
│
├── 📁 Account Service - Movements (6 requests)
│   ├── GET List Movements
│   ├── GET Movement by ID
│   ├── GET Movements by Account
│   ├── GET Movements by Customer
│   ├── POST Create Movement (Deposit/Withdrawal)
│   └── GET Health Check
│
└── 📁 Account Service - Reports (3 requests)
    ├── GET Report by Customer
    ├── GET Report by Account
    └── GET Health Check
```

**Total:** 25 endpoints funcionales + tests automáticos

---

## ⚙️ Configurar Variables

### Variables de Colección (Preconfiguradas)

La colección ya incluye las variables necesarias:

| Variable       | Valor                     | Descripción                       |
|----------------|---------------------------|-----------------------------------|
| `baseURLAuth`  | `http://localhost:8080`   | URL del servicio de autenticación |
| `baseURLCustomer` | `http://localhost:8081` | URL del servicio de clientes     |
| `baseURLAccount` | `http://localhost:8082`  | URL del servicio de cuentas      |
| `authToken`    | `(vacío)`                 | Token JWT (se guarda automáticamente) |

### Verificar / Editar Variables

1. **Click derecho** en la colección
2. **Edit**
3. **Pestaña "Variables"**
4. Verificar que las URLs sean correctas

### Cambiar para Otros Entornos

**Para desarrollo local sin Docker:**
```
baseURLAuth: http://localhost:8080
baseURLCustomer: http://localhost:8081
baseURLAccount: http://localhost:8082
```

**Para producción:**
```
baseURLAuth: https://api.banking.example.com
baseURLCustomer: https://api.banking.example.com
baseURLAccount: https://api.banking.example.com
```

---

## 🔄 Flujo de Pruebas Completo

### Secuencia Recomendada (Quick Start - 10 minutos)

```
┌─────────────────────────────────────────────────────────────┐
│  1. AUTENTICACIÓN (Obligatorio)                             │
│     POST /api/auth/login                                    │
│     ✅ Obtener token JWT                                    │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  2. CREAR CLIENTE (F1)                                      │
│     POST /api/v1/customers                                  │
│     📝 Body: {codigo, nombre, identificacion, password...}  │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  3. CREAR CUENTA (F2)                                       │
│     POST /api/v1/accounts                                   │
│     📝 Body: {numeroCuenta, tipo, saldoInicial, cliente}    │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  4. REGISTRAR DEPÓSITO (F2)                                 │
│     POST /api/v1/movements                                  │
│     📝 Body: {cuenta, tipo: "DEPOSITO", monto}              │
│     ✅ Saldo aumenta                                        │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  5. REGISTRAR RETIRO (F3)                                   │
│     POST /api/v1/movements                                  │
│     📝 Body: {cuenta, tipo: "RETIRO", monto}                │
│     ✅ Valida saldo disponible                              │
│     ❌ Si insuficiente → 400 "Saldo no disponible"          │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│  6. GENERAR REPORTE (F4)                                    │
│     GET /api/v1/reports/customer/{code}?from&to             │
│     📊 Estado de cuenta con movimientos                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 Casos de Prueba por Módulo

### 🔐 Auth Service (Port 8080)

#### 1. Login - Obtener Token JWT

**Request:**
```http
POST {{baseURLAuth}}/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

**Response 200 OK:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "admin",
  "expiresIn": 300
}
```

**Tests Automáticos:**
- ✅ Status code is 200
- ✅ Response contains "token"
- ✅ Token saved to `{{authToken}}`

**Credenciales disponibles:**
- `admin/admin` - Acceso completo
- `user/user` - Solo lectura

#### 2. Validate Token

**Request:**
```http
GET {{baseURLAuth}}/api/auth/validate
Authorization: Bearer {{authToken}}
```

**Response 200 OK:**
```json
{
  "username": "admin",
  "roles": ["ROLE_ADMIN"],
  "valid": true,
  "expiresAt": "2026-02-18T12:30:00"
}
```

---

### 👤 Customer Service (Port 8081) - F1

#### 1. Create Customer

**Request:**
```http
POST {{baseURLCustomer}}/api/v1/customers
Authorization: Bearer {{authToken}}
Content-Type: application/json

{
  "customerCode": "CLI100",
  "name": "Jose Lema",
  "gender": "M",
  "age": 30,
  "identification": "1234567890",
  "address": "Otavalo sn y principal",
  "phone": "098254785",
  "password": "1234",
  "status": true
}
```

**Response 201 Created:**
```json
{
  "id": 1,
  "customerCode": "CLI100",
  "name": "Jose Lema",
  "gender": "M",
  "age": 30,
  "identification": "1234567890",
  "address": "Otavalo sn y principal",
  "phone": "098254785",
  "status": true,
  "createdAt": "2026-02-18T10:30:00"
}
```

**Validaciones:**
- ❌ `customerCode` duplicado → 409 Conflict
- ❌ `identification` duplicado → 409 Conflict
- ❌ Campos requeridos faltantes → 400 Bad Request
- ❌ `age` < 18 → 400 Bad Request

#### 2. List Customers

**Request:**
```http
GET {{baseURLCustomer}}/api/v1/customers
Authorization: Bearer {{authToken}}
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "customerCode": "CLI100",
    "name": "Jose Lema",
    "status": true
  },
  {
    "id": 2,
    "customerCode": "CLI200",
    "name": "Marianela Montalvo",
    "status": true
  }
]
```

#### 3. Get Customer by ID

**Request:**
```http
GET {{baseURLCustomer}}/api/v1/customers/1
Authorization: Bearer {{authToken}}
```

**Response 200 OK:** (mismo formato que create)

**Error 404:**
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 404,
  "error": "Not Found",
  "mensaje": "Cliente no encontrado con ID: 999"
}
```

#### 4. Update Customer

**Request:**
```http
PUT {{baseURLCustomer}}/api/v1/customers/1
Authorization: Bearer {{authToken}}
Content-Type: application/json

{
  "customerCode": "CLI100",
  "name": "Jose Lema Actualizado",
  "gender": "M",
  "age": 31,
  "identification": "1234567890",
  "address": "Nueva dirección",
  "phone": "099999999",
  "password": "newpass123",
  "status": true
}
```

**Response 200 OK:** Cliente actualizado

#### 5. Delete Customer (Lógico)

**Request:**
```http
DELETE {{baseURLCustomer}}/api/v1/customers/1
Authorization: Bearer {{authToken}}
```

**Response 204 No Content**

**Comportamiento:**
- Marca `status = false`
- No elimina registro de BD
- Cliente puede ser reactivado

#### 6. Delete Customer (Físico)

**Request:**
```http
DELETE {{baseURLCustomer}}/api/v1/customers/1/physical
Authorization: Bearer {{authToken}}
```

**Response 204 No Content**

**Comportamiento:**
- Elimina registro de BD permanentemente
- ⚠️ No reversible
- ❌ Falla si tiene cuentas asociadas

---

### 💰 Account Service - Accounts (Port 8082) - F2

#### 1. Create Account

**Request:**
```http
POST {{baseURLAccount}}/api/v1/accounts
Authorization: Bearer {{authToken}}
Content-Type: application/json

{
  "accountNumber": "478758",
  "accountType": "AHORRO",
  "initialBalance": 2000.00,
  "status": true,
  "customerCode": "CLI100"
}
```

**Response 201 Created:**
```json
{
  "id": 1,
  "accountNumber": "478758",
  "accountType": "AHORRO",
  "initialBalance": 2000.00,
  "currentBalance": 2000.00,
  "status": true,
  "customerCode": "CLI100",
  "customerName": "Jose Lema",
  "createdAt": "2026-02-18T10:30:00"
}
```

**Validaciones:**
- ✅ `accountType` debe ser "AHORRO" o "CORRIENTE"
- ✅ `initialBalance` >= 0
- ❌ `accountNumber` duplicado → 409 Conflict
- ❌ `customerCode` no existe → 404 Not Found
- ❌ Cliente inactivo → 400 Bad Request

#### 2. List Accounts

**Request:**
```http
GET {{baseURLAccount}}/api/v1/accounts
Authorization: Bearer {{authToken}}
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "accountNumber": "478758",
    "accountType": "AHORRO",
    "currentBalance": 2000.00,
    "status": true,
    "customerName": "Jose Lema"
  }
]
```

#### 3. Get Account by Number

**Request:**
```http
GET {{baseURLAccount}}/api/v1/accounts/number/478758
Authorization: Bearer {{authToken}}
```

**Response 200 OK:** (formato completo)

---

### 💸 Account Service - Movements (Port 8082) - F2, F3

#### 1. Create Movement - Deposit (F2)

**Request:**
```http
POST {{baseURLAccount}}/api/v1/movements
Authorization: Bearer {{authToken}}
Content-Type: application/json

{
  "accountNumber": "478758",
  "movementType": "DEPOSITO",
  "amount": 600.00,
  "description": "Depósito en efectivo"
}
```

**Response 201 Created:**
```json
{
  "id": 1,
  "date": "2026-02-18T10:30:00",
  "movementType": "DEPOSITO",
  "amount": 600.00,
  "balance": 2600.00,
  "description": "Depósito en efectivo",
  "accountNumber": "478758"
}
```

**Comportamiento:**
- ✅ Suma `amount` al `currentBalance`
- ✅ `balance` en respuesta = saldo después del movimiento
- ✅ Crea registro en `ba_movimientos`
- ✅ Actualiza `ba_cuentas.cu_saldo_actual`

#### 2. Create Movement - Withdrawal with Validation (F3)

**Request - Retiro Válido:**
```http
POST {{baseURLAccount}}/api/v1/movements
Authorization: Bearer {{authToken}}
Content-Type: application/json

{
  "accountNumber": "478758",
  "movementType": "RETIRO",
  "amount": 300.00,
  "description": "Retiro en cajero"
}
```

**Response 201 Created:**
```json
{
  "id": 2,
  "date": "2026-02-18T10:35:00",
  "movementType": "RETIRO",
  "amount": 300.00,
  "balance": 2300.00,
  "description": "Retiro en cajero",
  "accountNumber": "478758"
}
```

**Request - Retiro sin Saldo (F3 Validation):**
```http
POST {{baseURLAccount}}/api/v1/movements
Authorization: Bearer {{authToken}}
Content-Type: application/json

{
  "accountNumber": "478758",
  "movementType": "RETIRO",
  "amount": 5000.00,
  "description": "Retiro excesivo"
}
```

**Response 400 Bad Request:**
```json
{
  "timestamp": "2026-02-18T10:40:00",
  "estado": 400,
  "error": "Saldo Insuficiente",
  "mensaje": "Saldo no disponible para cuenta: 478758"
}
```

**Validaciones F3:**
- ✅ **DEPOSITO**: Siempre permite (amount > 0)
- ✅ **RETIRO**: Valida `currentBalance >= amount`
- ❌ **Saldo insuficiente**: 400 Bad Request con mensaje "Saldo no disponible"
- ❌ **Amount <= 0**: 400 Bad Request
- ❌ **Cuenta inactiva**: 400 Bad Request
- ❌ **Cuenta no existe**: 404 Not Found

#### 3. List Movements

**Request:**
```http
GET {{baseURLAccount}}/api/v1/movements
Authorization: Bearer {{authToken}}
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "date": "2026-02-18T10:30:00",
    "movementType": "DEPOSITO",
    "amount": 600.00,
    "balance": 2600.00,
    "accountNumber": "478758"
  },
  {
    "id": 2,
    "date": "2026-02-18T10:35:00",
    "movementType": "RETIRO",
    "amount": 300.00,
    "balance": 2300.00,
    "accountNumber": "478758"
  }
]
```

#### 4. Get Movements by Account

**Request:**
```http
GET {{baseURLAccount}}/api/v1/movements/account/1
Authorization: Bearer {{authToken}}
```

**Response 200 OK:** Movimientos filtrados por cuenta

#### 5. Get Movements by Customer

**Request:**
```http
GET {{baseURLAccount}}/api/v1/movements/customer/CLI100
Authorization: Bearer {{authToken}}
```

**Response 200 OK:** Todos los movimientos de todas las cuentas del cliente

---

### 📊 Account Service - Reports (Port 8082) - F4

#### 1. Report by Customer with Dates (F4)

**Request:**
```http
GET {{baseURLAccount}}/api/v1/reports/customer/CLI100?from=2026-01-01T00:00:00&to=2026-12-31T23:59:59
Authorization: Bearer {{authToken}}
```

**Response 200 OK:**
```json
{
  "customerCode": "CLI100",
  "customerName": "Jose Lema",
  "reportPeriod": {
    "from": "2026-01-01T00:00:00",
    "to": "2026-12-31T23:59:59"
  },
  "accounts": [
    {
      "accountNumber": "478758",
      "accountType": "AHORRO",
      "initialBalance": 2000.00,
      "currentBalance": 2300.00,
      "status": true,
      "movements": [
        {
          "date": "2026-02-18T10:30:00",
          "movementType": "DEPOSITO",
          "amount": 600.00,
          "balance": 2600.00,
          "description": "Depósito en efectivo"
        },
        {
          "date": "2026-02-18T10:35:00",
          "movementType": "RETIRO",
          "amount": 300.00,
          "balance": 2300.00,
          "description": "Retiro en cajero"
        }
      ],
      "summary": {
        "totalDeposits": 600.00,
        "totalWithdrawals": 300.00,
        "netChange": 300.00,
        "movementCount": 2
      }
    }
  ],
  "totalSummary": {
    "totalAccountsBalance": 2300.00,
    "totalDeposits": 600.00,
    "totalWithdrawals": 300.00,
    "totalMovements": 2
  }
}
```

**Query Parameters:**
- `from` (opcional): Fecha inicio ISO-8601 (`2026-01-01T00:00:00`)
- `to` (opcional): Fecha fin ISO-8601 (`2026-12-31T23:59:59`)
- Si no se envían, muestra todos los movimientos históricos

#### 2. Report by Account with Dates

**Request:**
```http
GET {{baseURLAccount}}/api/v1/reports/account/478758?from=2026-02-01T00:00:00&to=2026-02-28T23:59:59
Authorization: Bearer {{authToken}}
```

**Response 200 OK:**
```json
{
  "accountNumber": "478758",
  "accountType": "AHORRO",
  "customerCode": "CLI100",
  "customerName": "Jose Lema",
  "reportPeriod": {
    "from": "2026-02-01T00:00:00",
    "to": "2026-02-28T23:59:59"
  },
  "initialBalance": 2000.00,
  "currentBalance": 2300.00,
  "movements": [
    {
      "date": "2026-02-18T10:30:00",
      "movementType": "DEPOSITO",
      "amount": 600.00,
      "balance": 2600.00,
      "description": "Depósito en efectivo"
    },
    {
      "date": "2026-02-18T10:35:00",
      "movementType": "RETIRO",
      "amount": 300.00,
      "balance": 2300.00,
      "description": "Retiro en cajero"
    }
  ],
  "summary": {
    "totalDeposits": 600.00,
    "totalWithdrawals": 300.00,
    "netChange": 300.00,
    "movementCount": 2
  }
}
```

---

## ⚠️ Troubleshooting

### Error 1: Connection Refused

**Síntoma:**
```
Error: connect ECONNREFUSED 127.0.0.1:8080
```

**Causas:**
- Servicios no están levantados
- Docker no está corriendo
- Puerto ocupado por otro proceso

**Solución:**
```bash
# Verificar Docker
docker ps

# Si está vacío, levantar servicios
docker-compose up -d --build

# Verificar puertos
netstat -ano | findstr :8080    # Windows
lsof -i :8080                   # Linux/Mac

# Ver logs si hay errores
docker-compose logs -f auth-service
```

---

### Error 2: 401 Unauthorized

**Síntoma:**
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 401,
  "error": "Unauthorized",
  "mensaje": "Full authentication is required"
}
```

**Causas:**
- Token JWT no enviado
- Token expirado (5 minutos de vida)
- Token inválido

**Solución:**

1. **Verificar que el token esté guardado:**
   - Ve a Variables de la colección
   - Verifica que `authToken` tenga un valor

2. **Obtener nuevo token:**
   - Ejecutar `POST /api/auth/login`
   - Copiar el token de la respuesta
   - Pegar en la variable `authToken`

3. **Verificar header Authorization:**
   ```
   Authorization: Bearer {{authToken}}
   ```

---

### Error 3: 400 Bad Request - Saldo no disponible (F3)

**Síntoma:**
```json
{
  "timestamp": "2026-02-18T10:40:00",
  "estado": 400,
  "error": "Saldo Insuficiente",
  "mensaje": "Saldo no disponible para cuenta: 478758"
}
```

**Causa:**
- Intento de retiro mayor al saldo disponible

**Solución:**

1. **Consultar saldo actual:**
   ```http
   GET {{baseURLAccount}}/api/v1/accounts/number/478758
   ```

2. **Ver campo `currentBalance` en la respuesta**

3. **Ajustar el monto del retiro o hacer un depósito primero**

---

### Error 4: 404 Not Found

**Síntoma:**
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 404,
  "error": "Not Found",
  "mensaje": "Cliente no encontrado con código: CLI999"
}
```

**Causas:**
- ID/Código no existe en la BD
- Typo en el identificador

**Solución:**

1. **Listar recursos existentes:**
   ```http
   GET {{baseURLCustomer}}/api/v1/customers
   GET {{baseURLAccount}}/api/v1/accounts
   ```

2. **Usar IDs/Códigos válidos**

3. **O crear el recurso primero con POST**

---

### Error 5: 409 Conflict - Recurso duplicado

**Síntoma:**
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 409,
  "error": "Conflict",
  "mensaje": "Ya existe un cliente con código: CLI100"
}
```

**Causa:**
- Intento de crear recurso con identificador único duplicado

**Solución:**

1. **Cambiar el identificador:**
   - `customerCode` → `CLI101`, `CLI102`, etc.
   - `accountNumber` → `478759`, `478760`, etc.
   - `identification` → Otro número de cédula

2. **O actualizar el existente con PUT**

---

### Error 6: Token Expiration (5 minutes)

**Síntoma:**
- Requests que funcionaban ahora retornan 401

**Causa:**
- JWT tiene vida de 5 minutos (configurado en `.env`)

**Solución:**

1. **Volver a hacer login:**
   ```http
   POST {{baseURLAuth}}/api/auth/login
   ```

2. **El nuevo token se guarda automáticamente**

3. **Continuar con las pruebas**

**Tip:** Mantén la pestaña de login abierta para renovar rápidamente

---

## 📊 Ejemplos de Respuestas

### ✅ Success Cases

#### 201 Created - Resource Created
```json
{
  "id": 1,
  "customerCode": "CLI100",
  "name": "Jose Lema",
  "status": true,
  "createdAt": "2026-02-18T10:30:00"
}
```

#### 200 OK - Resource Retrieved
```json
{
  "id": 1,
  "accountNumber": "478758",
  "accountType": "AHORRO",
  "currentBalance": 2600.00,
  "customerName": "Jose Lema"
}
```

#### 204 No Content - Resource Deleted
```
(Sin body, solo status code)
```

---

### ❌ Error Cases

#### 400 Bad Request - Invalid Input
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 400,
  "error": "Bad Request",
  "mensaje": "Parámetros de entrada inválidos",
  "detalles": {
    "amount": "debe ser mayor que 0",
    "accountNumber": "no puede estar vacío"
  }
}
```

#### 400 Bad Request - Business Rule (F3)
```json
{
  "timestamp": "2026-02-18T10:40:00",
  "estado": 400,
  "error": "Saldo Insuficiente",
  "mensaje": "Saldo no disponible para cuenta: 478758"
}
```

#### 401 Unauthorized - No Token
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 401,
  "error": "Unauthorized",
  "mensaje": "Full authentication is required"
}
```

#### 404 Not Found - Resource Not Exists
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 404,
  "error": "Not Found",
  "mensaje": "Cliente no encontrado con código: CLI999"
}
```

#### 409 Conflict - Duplicate Resource
```json
{
  "timestamp": "2026-02-18T10:30:00",
  "estado": 409,
  "error": "Conflict",
  "mensaje": "Ya existe un cliente con código: CLI100"
}
```

---

## 🎓 Tips y Mejores Prácticas

### 1. Organización de Requests

**Crear carpetas personalizadas:**
```
📁 My Tests
├── 📁 Happy Path (flujo normal)
│   ├── 1. Login
│   ├── 2. Create Customer
│   ├── 3. Create Account
│   ├── 4. Deposit
│   └── 5. Generate Report
│
└── 📁 Error Cases (validaciones)
    ├── Insufficient Balance
    ├── Duplicate Customer
    └── Invalid Token
```

### 2. Uso de Environments

**Crear múltiples entornos:**
- `Development` (localhost)
- `Staging` (servidor de pruebas)
- `Production` (producción)

**Switch rápido:** Dropdown en esquina superior derecha

### 3. Scripts Pre-request

**Auto-login si token expiró:**
```javascript
const tokenExpiration = pm.collectionVariables.get("tokenExpiration");
const now = new Date().getTime();

if (!tokenExpiration || now > tokenExpiration) {
    // Ejecutar login automáticamente
    pm.sendRequest({
        url: pm.collectionVariables.get("baseURLAuth") + "/api/auth/login",
        method: "POST",
        header: {"Content-Type": "application/json"},
        body: {
            mode: "raw",
            raw: JSON.stringify({username: "admin", password: "admin"})
        }
    }, (err, res) => {
        const token = res.json().token;
        pm.collectionVariables.set("authToken", token);
        pm.collectionVariables.set("tokenExpiration", now + 300000); // +5min
    });
}
```

### 4. Tests Avanzados

**Validar estructura de respuesta:**
```javascript
pm.test("Response has correct structure", () => {
    const response = pm.response.json();
    pm.expect(response).to.have.property("id");
    pm.expect(response).to.have.property("customerCode");
    pm.expect(response.status).to.be.a("boolean");
});
```

**Guardar IDs para requests subsiguientes:**
```javascript
if (pm.response.code === 201) {
    const response = pm.response.json();
    pm.collectionVariables.set("createdCustomerId", response.id);
    pm.collectionVariables.set("createdAccountNumber", response.accountNumber);
}
```

### 5. Runner para Flujos Completos

**Ejecutar toda la colección:**
1. Click en "..." en la colección
2. "Run collection"
3. Seleccionar requests a ejecutar
4. "Run NTT DATA Banking"
5. Ver reporte de ejecución

---

## 📞 Soporte

### Recursos Adicionales

- 📖 [README principal](../README.md)
- 🐛 [Troubleshooting](../README.md#-troubleshooting)
- 🧪 [Testing Guide](../README.md#-testing)
- 🏗️ [Arquitectura](../README.md#-arquitectura-del-sistema)

### Contacto

Si encuentras problemas no cubiertos en esta guía:

1. Revisa los logs de Docker: `docker-compose logs -f`
2. Verifica la consola de Postman (View → Show Postman Console)
3. Consulta el [README principal](../README.md)

---

## 📝 Notas Finales

### Datos Precargados

La base de datos incluye datos de prueba:

**Clientes:**
- `CLI100` - Jose Lema (identificación: 1234567890)
- `CLI200` - Marianela Montalvo (identificación: 0987654321)

**Cuentas:**
- `478758` - AHORRO - $2000 (CLI100)
- `225487` - CORRIENTE - $100 (CLI200)

### Limpieza de Datos

Para resetear la base de datos:

```bash
# Detener servicios
docker-compose down

# Eliminar volúmenes (borra datos)
docker-compose down -v

# Levantar de nuevo (recrea BD con datos iniciales)
docker-compose up -d --build
```

---

<div align="center">

**✨ ¡Listo para probar! ✨**

Comienza con el flujo básico: Login → Create Customer → Create Account → Deposit → Report

📊 Revisa los tests automáticos en la pestaña "Test Results" de Postman

</div>
