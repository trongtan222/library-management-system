# Copilot Instructions

## Architecture

**Stack**: Spring Boot 3.5 (Java 21) + Angular 14 SPA + MySQL + Redis

```
lms-backend/src/main/java/com/ibizabroker/lms/
├── controller/   # REST endpoints (@RestController)
├── service/      # Business logic (@Transactional)
├── dao/          # Spring Data JPA repositories
├── dto/          # Request/response objects with validation
├── entity/       # JPA entities
├── configuration/# Security, CORS, beans
└── exceptions/   # Custom exceptions

lms-frontend/src/app/
├── services/     # HTTP clients (inject into components)
├── auth/         # Guards, interceptors, JWT handling
└── [feature]/    # Feature modules (admin/, chatbot/, etc.)
```

## Security Patterns

- **Route protection** (`WebSecurityConfiguration.java`):
  - `/api/auth/**`, `/api/public/**` → no auth
  - `/api/user/**` → `hasAnyRole("USER", "ADMIN")`
  - `/api/admin/**` → `hasRole("ADMIN")`
- **Method-level security**: Use `@PreAuthorize` (NOT `@Secured`) — see existing controllers
  - Admin: `@PreAuthorize("hasRole('ADMIN')")` or `@PreAuthorize("hasRole('ROLE_ADMIN')")`
  - User: `@PreAuthorize("hasRole('USER')")` or `@PreAuthorize("isAuthenticated()")`
- **JWT**: Handled by `JwtRequestFilter` → `JwtUtil`; token in `Authorization: Bearer <token>`

## Code Conventions

- **Services**: Constructor injection with `@RequiredArgsConstructor` (Lombok); mark read-only methods `@Transactional(readOnly = true)`
- **Controllers return DTOs**, not entities—avoid lazy-loading issues and data exposure
- **Validation**: Use Jakarta annotations (`@NotEmpty`, `@Min`, etc.) in DTOs; errors caught by `ApiExceptionHandler`
- **Error handling**: Throw `NotFoundException`, `IllegalStateException`, etc.—`ApiExceptionHandler` converts to JSON with HTTP status
- **N+1 fix pattern**: Use `@EntityGraph` or `@BatchSize(size = 20)` on entity collections (see `Books.java`, `BooksRepository.java`)
- **Vietnamese locale**: Some error messages and DTOs use Vietnamese—maintain consistency or ask user for preference

## Build & Run

```bash
# Backend (requires MySQL on 3306/3307, optional Redis on 6379)
cd lms-backend && mvn spring-boot:run -Dspring.profiles.active=dev

# Frontend (dev server on :4200)
cd lms-frontend && npm install && npm start

# Full stack via Docker
docker-compose up --build  # backend:8081, frontend:80, MySQL:3308, Redis:6379
```

## Environment Variables

| Variable                 | Default                              | Purpose                        |
| ------------------------ | ------------------------------------ | ------------------------------ |
| `DB_URL`                 | `jdbc:mysql://localhost:3307/lms_db` | MySQL connection               |
| `APP_JWT_SECRET`         | dev fallback                         | JWT signing key (32+ bytes)    |
| `GEMINI_API_KEY`         | none                                 | Chatbot AI (required for chat) |
| `REDIS_HOST/PORT`        | localhost:6379                       | Rate limiting & cache          |
| `MAIL_USERNAME/PASSWORD` | none                                 | Password reset emails          |

Use `application-dev.properties` for local overrides; activate with `SPRING_PROFILES_ACTIVE=dev`.

## Key Files

| Purpose                    | File                                                            |
| -------------------------- | --------------------------------------------------------------- |
| Security config            | `configuration/WebSecurityConfiguration.java`                   |
| JWT utils                  | `util/JwtUtil.java`                                             |
| Global error handler       | `controller/ApiExceptionHandler.java`                           |
| Chatbot flow               | `controller/ChatbotController.java` → `service/RagService.java` |
| Entity example (N+1 fixed) | `entity/Books.java`, `dao/BooksRepository.java`                 |
| Frontend API base          | `environments/environment.ts` (`apiBaseUrl`)                    |
| Frontend JWT interceptor   | `auth/auth.interceptor.ts`                                      |

## Common Tasks

- **Add new endpoint**: Create controller method → add service method → use DTO for request/response
- **Add admin-only endpoint**: Use `@PreAuthorize("hasRole('ADMIN')")` at method or class level
- **Add new entity**: Create entity in `entity/`, repository in `dao/`, then service/controller as needed
- **Run tests**: `cd lms-backend && mvn test` (requires running MySQL)
- **Debug auth issues**: Check `JwtRequestFilter` logs, verify token format, check role names (with/without `ROLE_` prefix)
