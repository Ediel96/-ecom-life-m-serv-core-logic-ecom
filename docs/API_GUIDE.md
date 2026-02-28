# Guía de API - Life Organizer

## 🔑 Autenticación

Todas las APIs requieren un token JWT en el header:

```
Authorization: Bearer <tu_token_jwt>
```

### Obtener Token (Login)

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@email.com",
    "password": "tu_password"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "f1abf483-8dab-4198-bea9-c3ca52e93019",
  "role": "ROLE_USER"
}
```

---

## 📊 Accounts API

### Listar Cuentas

```bash
curl -X GET http://localhost:8080/accounts \
  -H "Authorization: Bearer {token}"
```

**Parámetros opcionales:**
- `userId`: UUID del usuario (solo admin)

**Respuesta:**
```json
[
  {
    "id": 1001,
    "userId": "f1abf483-8dab-4198-bea9-c3ca52e93019",
    "name": "Cuenta Débito",
    "accountType": "debit",
    "balance": 1500000.00,
    "currency": "COP",
    "type": "bank",
    "bankName": "Bancolombia",
    "createdAt": "2026-01-06T03:00:40.136Z",
    "updatedAt": "2026-02-28T10:00:00.000Z"
  }
]
```

### Obtener Cuenta por ID

```bash
curl -X GET http://localhost:8080/accounts/1001 \
  -H "Authorization: Bearer {token}"
```

### Crear Cuenta

```bash
curl -X POST http://localhost:8080/accounts \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cuenta de Ahorros",
    "accountType": "savings",
    "balance": 0,
    "currency": "COP",
    "type": "bank",
    "bankName": "Davivienda",
    "isActivated": true
  }'
```

### Actualizar Cuenta

```bash
curl -X PUT http://localhost:8080/accounts/1001 \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cuenta Principal",
    "balance": 2000000
  }'
```

### Eliminar Cuenta

```bash
curl -X DELETE http://localhost:8080/accounts/1001 \
  -H "Authorization: Bearer {token}"
```

---

## 🏷️ Categories API

### Listar Categorías

```bash
# Todas las categorías
curl -X GET http://localhost:8080/categories \
  -H "Authorization: Bearer {token}"

# Solo gastos
curl -X GET "http://localhost:8080/categories?transactionType=expense" \
  -H "Authorization: Bearer {token}"

# Solo ingresos
curl -X GET "http://localhost:8080/categories?transactionType=income" \
  -H "Authorization: Bearer {token}"
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "key": "food",
    "name": "Alimentación",
    "colorFill": "#FF5733",
    "colorBg": "#FFEEEE",
    "icon": "restaurant",
    "transactionType": "expense",
    "createdAt": "2026-01-06T03:00:40.136Z",
    "updatedAt": "2026-01-06T03:00:40.136Z"
  },
  {
    "id": 2,
    "key": "transport",
    "name": "Transporte",
    "colorFill": "#3498DB",
    "colorBg": "#EBF5FB",
    "icon": "directions_car",
    "transactionType": "expense",
    "createdAt": "2026-01-06T03:00:40.136Z",
    "updatedAt": "2026-01-06T03:00:40.136Z"
  }
]
```

### Obtener Categoría por ID

```bash
curl -X GET http://localhost:8080/categories/1 \
  -H "Authorization: Bearer {token}"
```

### Crear Categoría

```bash
curl -X POST http://localhost:8080/categories \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "key": "entertainment",
    "name": "Entretenimiento",
    "colorFill": "#9B59B6",
    "colorBg": "#F5EEF8",
    "icon": "movie",
    "transactionType": "expense"
  }'
```

### Actualizar Categoría

```bash
curl -X PUT http://localhost:8080/categories/1 \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Comida y Bebidas",
    "colorFill": "#E74C3C"
  }'
```

### Eliminar Categoría

```bash
curl -X DELETE http://localhost:8080/categories/1 \
  -H "Authorization: Bearer {token}"
```

---

## 💰 Transactions API

### Listar Transacciones (Paginado)

```bash
# Básico
curl -X GET http://localhost:8080/transactions \
  -H "Authorization: Bearer {token}"

# Con paginación
curl -X GET "http://localhost:8080/transactions?page=0&size=10" \
  -H "Authorization: Bearer {token}"

# Con filtros
curl -X GET "http://localhost:8080/transactions?transactionType=expense&categoryId=1" \
  -H "Authorization: Bearer {token}"

# Con rango de fechas
curl -X GET "http://localhost:8080/transactions?dateFrom=2026-02-01T00:00:00Z&dateTo=2026-02-28T23:59:59Z" \
  -H "Authorization: Bearer {token}"

# Con ordenamiento
curl -X GET "http://localhost:8080/transactions?sort=amount,desc" \
  -H "Authorization: Bearer {token}"
```

**Parámetros de consulta:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `page` | Integer | Número de página (default: 0) |
| `size` | Integer | Elementos por página (default: 20) |
| `sort` | String | Ordenamiento: `campo,direccion` |
| `userId` | UUID | Filtrar por usuario |
| `accountId` | Integer | Filtrar por cuenta |
| `categoryId` | Integer | Filtrar por categoría |
| `transactionType` | String | `expense` o `income` |
| `dateFrom` | DateTime | Fecha inicio (ISO 8601) |
| `dateTo` | DateTime | Fecha fin (ISO 8601) |

**Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "accountId": 1001,
      "userId": "f1abf483-8dab-4198-bea9-c3ca52e93019",
      "categoryId": 1,
      "amount": 50000.00,
      "transactionType": "expense",
      "description": "Almuerzo restaurante",
      "date": "2026-02-28T12:30:00.000Z",
      "notification": false,
      "frequency": "none",
      "lifestyle": false,
      "createdAt": "2026-02-28T12:35:00.000Z",
      "updatedAt": "2026-02-28T12:35:00.000Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8
}
```

### Obtener Transacción por ID

```bash
curl -X GET http://localhost:8080/transactions/1 \
  -H "Authorization: Bearer {token}"
```

### Crear Transacción

```bash
curl -X POST http://localhost:8080/transactions \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 1001,
    "categoryId": 1,
    "amount": 75000,
    "transactionType": "expense",
    "description": "Mercado semanal",
    "date": "2026-02-28T10:00:00Z",
    "notification": true,
    "notificationDate": "2026-03-01T08:00:00Z",
    "frequency": "week",
    "lifestyle": true
  }'
```

**Campos:**

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `accountId` | Integer | Sí | ID de la cuenta |
| `categoryId` | Integer | Sí | ID de la categoría |
| `amount` | Number | Sí | Monto de la transacción |
| `transactionType` | String | Sí | `expense` o `income` |
| `description` | String | No | Descripción |
| `date` | DateTime | Sí | Fecha de la transacción |
| `notification` | Boolean | No | Activar notificación |
| `notificationDate` | DateTime | No | Fecha de notificación |
| `frequency` | String | No | Frecuencia de repetición |
| `lifestyle` | Boolean | No | Es gasto de estilo de vida |

**Valores de `frequency`:**
- `none` - Sin repetición
- `day` - Diario
- `week` - Semanal
- `month` - Mensual
- `year` - Anual
- `weekend` - Fines de semana
- `monday` a `sunday` - Día específico
- `custom` - Personalizado

### Actualizar Transacción

```bash
curl -X PUT http://localhost:8080/transactions/1 \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 80000,
    "description": "Mercado semanal actualizado"
  }'
```

### Eliminar Transacción

```bash
curl -X DELETE http://localhost:8080/transactions/1 \
  -H "Authorization: Bearer {token}"
```

---

## 📝 Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| 200 | OK - Solicitud exitosa |
| 201 | Created - Recurso creado |
| 204 | No Content - Eliminación exitosa |
| 400 | Bad Request - Datos inválidos |
| 401 | Unauthorized - Token faltante o inválido |
| 403 | Forbidden - Sin permisos |
| 404 | Not Found - Recurso no encontrado |
| 500 | Internal Server Error |

## 🔧 Manejo de Errores

**Respuesta de error:**
```json
{
  "timestamp": "2026-02-28T10:00:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "El campo 'amount' es requerido",
  "path": "/transactions"
}
```

---

## 📥 Colección Postman

Puedes importar todos estos endpoints en Postman:

1. Crear nueva colección "Life Organizer API"
2. Configurar variable de entorno `{{base_url}}` = `http://localhost:8080`
3. Configurar variable `{{token}}` con tu JWT
4. Importar cada endpoint

### Variables de Entorno Postman

```json
{
  "base_url": "http://localhost:8080",
  "token": "tu_jwt_token_aqui"
}
```

