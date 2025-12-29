# Copilot Instructions

- **Architecture**: Spring Boot 3.5 (Java 21) monolith in `lms-backend` plus Angular 14 SPA in `lms-frontend`; MySQL for persistence, Redis for chatbot cache/rate-limit, optional Gmail SMTP; OpenAPI served at `/swagger-ui`.
- **Backend layout**: Controllers in `controller/` map to services in `service/` and repositories in `dao/`; DTOs in `dto/`; entities in `entity/`; security + CORS/JWT in `configuration/`; utility JWT helpers in `util/`.
- **Security model**: JWT auth via `JwtRequestFilter` and `JwtUtil`; public routes are `/api/auth/**` and `/api/public/**`; `/api/user/**` requires `ROLE_USER` or `ROLE_ADMIN`; `/api/admin/**` requires `ROLE_ADMIN`; chatbot at `/api/user/chat/**` is authenticated; CORS allows `http://localhost:4200`.
- **Chatbot flow**: `ChatbotController` builds context from `ConversationService` (Redis-backed history cache, DB fallback) and `RagService` keyword-based book context, then calls Google Gemini (`gemini.api.key` or service account). `ChatRateLimiter` (Redis preferred; memory fallback) enforces `chat.rate.*` props; adjust env if throttling.
- **Data seeding**: `application.properties` disables SQL init; `application-dev.properties` enables `spring.sql.init.mode=always` and `data.sql` sample catalog. Use `SPRING_PROFILES_ACTIVE=dev` when you need seed data locally.
- **Database defaults**: JDBC URL defaults to `localhost:3307/lms_db`; docker-compose maps MySQL on host `3308` -> container `3306` (update URL accordingly). Credentials default to root/no password unless overridden.
- **JWT settings**: Configure `APP_JWT_SECRET` (dev fallback present) and `app.jwt-expiration`; tokens carry `userId` + roles claims and power method security.
- **Redis**: Defaults `localhost:6379`; docker-compose service named `redis`. Missing Redis triggers in-memory fallback for rate limiting but conversation caching will fail.
- **Mail**: Uses `spring-boot-starter-mail`; supply `MAIL_USERNAME`/`MAIL_PASSWORD` for password reset flows.
- **Angular client**: API base set in `src/environments/environment*.ts` (`apiBaseUrl`); dev uses `http://localhost:8080/api`. Google Books API key placeholder present—do not commit secrets.
- **Build/test commands**: Java 21 enforced by Maven Enforcer. Typical flow: `cd lms-backend && mvn clean test` (requires DB up), `mvn spring-boot:run` for dev; `cd lms-frontend && npm install && ng serve`. Docker: `docker-compose up --build` exposes backend `8081`, frontend `80`, MySQL `3308`, Redis `6379`.
- **Logging**: Logs to `logs/application.log`; dev profile increases verbosity (`DEBUG` for web/security/jpa). Adjust via `application-dev.properties`.
- **Notable controllers**: `AuthController` handles register/login/forgot/reset; `BooksController`, `Admin*Controller`, `CirculationController`, `WishlistController`, `ReportController`, `ReviewController`, `ChatbotController` cover domain endpoints; `ApiExceptionHandler` centralizes error responses.
- **Service conventions**: Business logic sits in `service/` with `@Transactional` boundaries; repositories are Spring Data interfaces; prefer DTOs in request/response bodies rather than entities.
- **RAG details**: `RagService` currently does simple keyword search over books/authors/categories and formats context; consider this when improving answers or augmenting prompts.
- **Testing/data**: Tests in `src/test/java`; many integration tests expect database connectivity. Use dev profile or provide test containers/DB to avoid failures.
- **Front/back integration**: JWT is stored client-side and sent as `Authorization: Bearer <token>`; forbidden redirects handled in Angular guards/components under `app/auth` and `app/forbidden`.
- **Ports & URLs**: Backend defaults 8080, frontend dev 4200; docker-compose backend 8081. Update CORS origins if you change frontend host/port.
- **Performance/limits**: Chat endpoints may return 429 when `ChatRateLimiter` thresholds hit; tweak `chat.rate.window-millis` and `chat.rate.max-requests` in env.
- **Docs/shortcuts**: Root `README.md` covers stack and setup; `docker-compose.yml` captures full stack; API docs at `/swagger-ui` once backend is running.

Suggest improvements or flag gaps if anything here feels incomplete or outdated.
