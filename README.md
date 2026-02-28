# Life Organizer - Core Logic Microservice

## 📋 Descripción

Microservicio backend para la aplicación Life Organizer, una plataforma de gestión financiera personal. Este servicio proporciona APIs REST para gestionar cuentas, categorías y transacciones financieras.

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Java | 20 | Lenguaje de programación |
| Spring Boot | 3.5.x | Framework principal |
| PostgreSQL | 15+ | Base de datos |
| Liquibase | 4.23.x | Migraciones de base de datos |
| JWT | - | Autenticación |
| OpenAPI | 3.0 | Documentación de API |
| Gradle | 8.x | Build tool |
| Docker | - | Contenedorización |

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/life_ecom_logic_core/eddie/
│   │   ├── EddieApplication.java          # Punto de entrada
│   │   ├── config/                         # Configuraciones
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── JwtUtil.java
│   │   │   ├── SecurityConfig.java
│   │   │   └── WebConfig.java
│   │   ├── controller/                     # Controladores API
│   │   │   ├── AccountsController.java
│   │   │   ├── CategoryController.java
│   │   │   └── TransactionsController.java
│   │   ├── converter/                      # Convertidores
│   │   ├── domain/                         # Entidades JPA
│   │   │   ├── AccountEntity.java
│   │   │   ├── CategoryEntity.java
│   │   │   ├── TransactionEntity.java
│   │   │   └── UserEntity.java
│   │   ├── repository/                     # Repositorios JPA
│   │   └── service/                        # Servicios
│   │       ├── Impl/                       # Implementaciones
│   │       └── mapper/                     # Mappers DTO-Entity
│   └── resources/
│       ├── application.properties          # Configuración
│       ├── api.yaml                         # OpenAPI spec
│       └── db/
│           ├── master.yaml                  # Liquibase master
│           └── changelog/                   # Migraciones
└── test/                                    # Tests
```

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 20+
- PostgreSQL 15+
- Docker (opcional)

### Configuración de Base de Datos

1. Crear base de datos PostgreSQL:
```sql
CREATE DATABASE life_organizer;
```

2. Configurar variables de entorno o modificar `application.properties`:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/life_organizer
export DB_USER=your_user
export DB_PASSWORD=your_password
export JWT_SECRET=your_base64_encoded_secret
```

### Ejecutar la Aplicación

#### Con Gradle
```bash
./gradlew bootRun
```

#### Con Docker
```bash
docker-compose up -d
```

## 📚 API Endpoints

### Autenticación
Todas las APIs requieren autenticación JWT Bearer token excepto las indicadas.

### Accounts API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/accounts` | Lista todas las cuentas del usuario |
| GET | `/accounts/{id}` | Obtiene una cuenta específica |
| POST | `/accounts` | Crea una nueva cuenta |
| PUT | `/accounts/{id}` | Actualiza una cuenta |
| DELETE | `/accounts/{id}` | Elimina una cuenta |

#### Ejemplo: Crear cuenta
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cuenta Principal",
    "accountType": "debit",
    "balance": 1000.00,
    "currency": "COP",
    "type": "bank",
    "bankName": "Bancolombia"
  }'
```

### Categories API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/categories` | Lista todas las categorías |
| GET | `/categories?transactionType=expense` | Lista categorías por tipo |
| GET | `/categories/{id}` | Obtiene una categoría |
| POST | `/categories` | Crea una categoría |
| PUT | `/categories/{id}` | Actualiza una categoría |
| DELETE | `/categories/{id}` | Elimina una categoría |

#### Ejemplo: Crear categoría
```bash
curl -X POST http://localhost:8080/categories \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "key": "food",
    "name": "Alimentación",
    "colorFill": "#FF5733",
    "colorBg": "#FFEEEE",
    "icon": "restaurant",
    "transactionType": "expense"
  }'
```

### Transactions API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/transactions` | Lista transacciones (paginado) |
| GET | `/transactions/{id}` | Obtiene una transacción |
| POST | `/transactions` | Crea una transacción |
| PUT | `/transactions/{id}` | Actualiza una transacción |
| DELETE | `/transactions/{id}` | Elimina una transacción |

#### Parámetros de Consulta (GET /transactions)
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 20)
- `sort`: Campo de ordenamiento (ej: `amount,desc`)
- `userId`: Filtrar por usuario
- `accountId`: Filtrar por cuenta
- `categoryId`: Filtrar por categoría
- `transactionType`: `expense` | `income`
- `dateFrom`: Fecha inicio (ISO 8601)
- `dateTo`: Fecha fin (ISO 8601)

#### Ejemplo: Crear transacción
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 1,
    "categoryId": 5,
    "amount": 50000,
    "transactionType": "expense",
    "description": "Compra supermercado",
    "date": "2026-02-28T10:00:00Z",
    "notification": true,
    "frequency": "none",
    "lifestyle": false
  }'
```

## 🔐 Seguridad

### JWT Configuration
El servicio utiliza JWT para autenticación. El token debe incluirse en el header:
```
Authorization: Bearer <token>
```

### Claims del Token
- `sub`: Username
- `userId`: UUID del usuario
- `role`: Rol del usuario (ROLE_USER, ROLE_ADMIN)

## 🗃️ Base de Datos

### Migraciones (Liquibase)
Las migraciones se ejecutan automáticamente al iniciar la aplicación.

```
db/changelog/
├── 01-create-enums.yaml
├── 02-create-table-categories.yaml
├── 02-create-table-users.yaml
├── 03-create-table-accounts.yaml
├── 04-create-table-transactions.yaml
├── 05-create-table-future-plans.yaml
├── ...
└── seeds/
    ├── 01-categories.sql
    ├── 02-users.sql
    └── 03-accounts.sql
```

### Tipos Enumerados PostgreSQL
- `transaction_type`: expense, income
- `frequency_type`: none, day, week, month, year, weekend, custom, monday-sunday

## 🧪 Testing

```bash
# Ejecutar todos los tests
./gradlew test

# Ejecutar tests con cobertura
./gradlew test jacocoTestReport
```

## 🔧 Configuración

### Variables de Entorno

| Variable | Descripción | Default |
|----------|-------------|---------|
| `DB_URL` | URL de conexión PostgreSQL | `jdbc:postgresql://localhost:5432/life_organizer` |
| `DB_USER` | Usuario de BD | `root` |
| `DB_PASSWORD` | Contraseña de BD | `your_password` |
| `JWT_SECRET` | Secreto JWT (Base64) | - |
| `JWT_EXPIRATION_MS` | Expiración del token en ms | `36000000` (10 horas) |

### Perfiles Spring
- `dev`: Perfil de desarrollo (default)
- `prod`: Perfil de producción

## 📦 Build y Deployment

### Build JAR
```bash
./gradlew build
```

### Build Docker Image
```bash
docker build -t life-organizer-core:latest .
```

### Docker Compose
```bash
docker-compose up -d
```

## 🤝 Contribución

1. Fork el repositorio
2. Crear branch feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📄 Licencia

Este proyecto es privado y confidencial.

## 👤 Autor

- **Eddie** - Desarrollo principal

---

## 📞 Soporte

Para soporte, contactar al equipo de desarrollo.

