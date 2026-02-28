# Documentación de Arquitectura - Life Organizer Core

## 📐 Arquitectura General

Este microservicio sigue una arquitectura en capas (Layered Architecture) con los siguientes componentes:

```
┌─────────────────────────────────────────────────────────────┐
│                        API Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Accounts   │  │ Categories  │  │    Transactions     │ │
│  │ Controller  │  │ Controller  │  │     Controller      │ │
│  └──────┬──────┘  └──────┬──────┘  └──────────┬──────────┘ │
└─────────┼────────────────┼─────────────────────┼───────────┘
          │                │                     │
┌─────────▼────────────────▼─────────────────────▼───────────┐
│                     Service Layer                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Account    │  │  Category   │  │    Transactions     │ │
│  │  Service    │  │  Service    │  │      Service        │ │
│  └──────┬──────┘  └──────┬──────┘  └──────────┬──────────┘ │
└─────────┼────────────────┼─────────────────────┼───────────┘
          │                │                     │
┌─────────▼────────────────▼─────────────────────▼───────────┐
│                     Mapper Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Account    │  │  Category   │  │    Transactions     │ │
│  │   Mapper    │  │   Mapper    │  │       Mapper        │ │
│  └──────┬──────┘  └──────┬──────┘  └──────────┬──────────┘ │
└─────────┼────────────────┼─────────────────────┼───────────┘
          │                │                     │
┌─────────▼────────────────▼─────────────────────▼───────────┐
│                   Repository Layer                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Account    │  │  Category   │  │    Transaction      │ │
│  │ Repository  │  │ Repository  │  │     Repository      │ │
│  └──────┬──────┘  └──────┬──────┘  └──────────┬──────────┘ │
└─────────┼────────────────┼─────────────────────┼───────────┘
          │                │                     │
┌─────────▼────────────────▼─────────────────────▼───────────┐
│                      Database                               │
│                   ┌─────────────┐                          │
│                   │  PostgreSQL │                          │
│                   └─────────────┘                          │
└─────────────────────────────────────────────────────────────┘
```

## 🔄 Flujo de Datos

### Request Flow
```
HTTP Request
     │
     ▼
┌─────────────────┐
│ Security Filter │  ─── JWT Validation
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Controller    │  ─── Request validation & routing
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Service      │  ─── Business logic
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Mapper       │  ─── DTO ↔ Entity conversion
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Repository    │  ─── Data access
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Database      │
└─────────────────┘
```

## 📦 Componentes Principales

### Controllers
Implementan interfaces generadas desde OpenAPI (`*ApiDelegate`).

```java
@Service
@RequiredArgsConstructor
public class AccountsController implements AccountsApiDelegate {
    private final AccountService accountService;
    // Métodos CRUD
}
```

### Services
Contienen la lógica de negocio y son inyectados en los controladores.

```java
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;
    // Implementación de lógica
}
```

### Mappers
Transforman entre DTOs (API) y Entities (JPA).

```java
public class AccountMapper {
    public static Account toDto(AccountEntity entity) { ... }
    public static AccountEntity toEntity(AccountCreate create, UserEntity user) { ... }
}
```

### Repositories
Interfaces Spring Data JPA para acceso a datos.

```java
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByUserId(UUID userId, String type);
}
```

## 🔐 Seguridad

### Flujo de Autenticación
```
┌──────────┐     ┌──────────────┐     ┌────────────┐
│  Client  │────►│ Auth Service │────►│  JWT Token │
└──────────┘     └──────────────┘     └─────┬──────┘
                                            │
                                            ▼
                                    ┌──────────────┐
                                    │ API Request  │
                                    │ + Bearer JWT │
                                    └───────┬──────┘
                                            │
                                            ▼
                                    ┌──────────────┐
                                    │Security Chain│
                                    │ JWT Decoder  │
                                    └───────┬──────┘
                                            │
                                            ▼
                                    ┌──────────────┐
                                    │  Controller  │
                                    └──────────────┘
```

### Configuración de Seguridad
- OAuth2 Resource Server con JWT
- CORS configurado para frontend
- Endpoints públicos: `/error`, OPTIONS requests
- Stateless session management

## 🗃️ Modelo de Datos

### Entidades Principales

```
┌─────────────────┐     ┌─────────────────┐
│      Users      │     │   Categories    │
├─────────────────┤     ├─────────────────┤
│ id (UUID)       │     │ id (INT)        │
│ name            │     │ key             │
│ email           │     │ name            │
│ password        │     │ colorFill       │
│ role            │     │ colorBg         │
└────────┬────────┘     │ icon            │
         │              │ transactionType │
         │              └────────┬────────┘
         │                       │
         ▼                       ▼
┌─────────────────┐     ┌─────────────────┐
│    Accounts     │     │  Transactions   │
├─────────────────┤     ├─────────────────┤
│ id (LONG)       │◄────│ account_id      │
│ user_id (FK)    │     │ user_id (FK)    │
│ name            │     │ category_id(FK) │
│ accountType     │     │ amount          │
│ balance         │     │ transactionType │
│ currency        │     │ description     │
│ bankName        │     │ date            │
└─────────────────┘     │ frequency       │
                        │ notification    │
                        │ lifestyle       │
                        └─────────────────┘
```

### Enumeraciones (PostgreSQL)

```sql
-- Tipo de transacción
CREATE TYPE transaction_type AS ENUM ('expense', 'income');

-- Frecuencia
CREATE TYPE frequency_type AS ENUM (
    'none', 'day', 'week', 'month', 'year', 
    'weekend', 'custom',
    'monday', 'tuesday', 'wednesday', 
    'thursday', 'friday', 'saturday', 'sunday'
);
```

## 🔧 Configuración

### Propiedades Principales

| Grupo | Propiedad | Descripción |
|-------|-----------|-------------|
| Datasource | `spring.datasource.url` | URL de conexión |
| JPA | `spring.jpa.properties.hibernate.dialect` | Dialecto PostgreSQL |
| JWT | `jwt.secret` | Secreto para firmar tokens |
| JWT | `jwt.expiration-ms` | Tiempo de expiración |
| CORS | `cors.allowed-origins` | Orígenes permitidos |
| Liquibase | `spring.liquibase.change-log` | Archivo maestro |

### Profiles

**Development (dev)**
- Logging DEBUG habilitado
- Stack traces completos en errores
- Migraciones automáticas

**Production (prod)**
- Logging INFO/WARN
- Errores sanitizados
- Conexión a BD productiva

## 📊 Patrones de Diseño Utilizados

1. **Delegate Pattern**: Controllers implementan interfaces `*ApiDelegate`
2. **Repository Pattern**: Abstracción de acceso a datos
3. **DTO Pattern**: Separación entre API y persistencia
4. **Dependency Injection**: Inyección por constructor
5. **Builder Pattern**: Construcción de entidades

## 🚀 Extensibilidad

### Agregar Nueva Entidad

1. **Definir Entity** en `domain/`
2. **Crear Repository** en `repository/`
3. **Implementar Service** en `service/Impl/`
4. **Crear Mapper** en `service/mapper/`
5. **Agregar API en OpenAPI** spec
6. **Implementar Controller** delegate

### Agregar Nuevo Endpoint

1. Modificar `api.yaml`
2. Regenerar código OpenAPI
3. Implementar método en Controller existente

## 📈 Monitoreo

### Endpoints de Health
- `/actuator/health` - Estado de la aplicación
- `/actuator/info` - Información de la aplicación

### Logging
```properties
logging.level.org.springframework.security=DEBUG
logging.level.liquibase=DEBUG
```

