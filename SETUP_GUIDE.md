# Development Setup Guide

## Prerequisites

### Backend Requirements
- **Java 21 (LTS)** - Required for the backend
- **Maven 3.8+** - Build tool
- **MySQL 8.0+** - Database

### Frontend Requirements
- **Node.js 20+** - JavaScript runtime
- **npm 10+** - Package manager
- **Angular CLI 20+** - Angular development tool

## Backend Setup

### 1. Install Java 21

**Windows (using Adoptium/Temurin):**
```powershell
# Using winget
winget install --id EclipseAdoptium.Temurin.21.JDK -e

# Or download from https://adoptium.net/releases.html
# Then set environment variables:
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
java -version
```

**macOS (using Homebrew):**
```bash
brew tap homebrew/cask-versions
brew install temurin@21
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install temurin-21-jdk
```

### 2. Configure MySQL Database

```bash
# Start MySQL service
mysql -u root -p

# Create database
CREATE DATABASE lms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lms_db;

# Exit MySQL
EXIT;
```

### 3. Configure Environment Variables

```bash
cd lms-backend

# Copy .env.example to .env
cp .env.example .env

# Edit .env with your configuration
# Important: NEVER commit .env to git!
```

**Example .env:**
```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password

APP_JWT_SECRET=your-minimum-32-character-secret-key-here
APP_JWT_EXPIRATION=86400000

SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password

GEMINI_API_KEY=your-gemini-api-key

SERVER_PORT=8080
```

### 4. Build and Run Backend

```bash
cd lms-backend

# Install dependencies
mvn clean install

# Run the application
mvn spring-boot:run

# Or build and run JAR
mvn clean package -DskipTests
java -jar target/lms-0.0.1-SNAPSHOT.jar
```

Backend will be available at: `http://localhost:8080`

## Frontend Setup

### 1. Install Node.js

**Windows:**
- Download from https://nodejs.org/ (LTS version)
- Run installer and follow prompts
- Verify: `node --version` and `npm --version`

**macOS (using Homebrew):**
```bash
brew install node
node --version
npm --version
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install nodejs npm
```

### 2. Install Angular CLI

```bash
npm install -g @angular/cli@20
ng version
```

### 3. Install Dependencies

```bash
cd lms-frontend

# Install npm packages
npm install
```

### 4. Configure API URLs

The frontend automatically uses configuration from `src/environments/environment.ts`:

**Development** (`environment.ts`):
```typescript
apiBaseUrl: 'http://localhost:8080'
```

**Production** (`environment.prod.ts`):
```typescript
apiBaseUrl: 'https://your-production-api.com'
```

### 5. Run Frontend

```bash
cd lms-frontend

# Development server with hot reload
ng serve

# Or with production build
ng build --configuration production
```

Frontend will be available at: `http://localhost:4200`

## Running Both Applications

### Option 1: Two Terminal Windows

**Terminal 1 - Backend:**
```bash
cd lms-backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd lms-frontend
ng serve
```

### Option 2: Using Docker (Recommended for Production)

```bash
# Build Docker images
docker build -t lms-backend ./lms-backend
docker build -t lms-frontend ./lms-frontend

# Run with docker-compose
docker-compose up -d
```

## Testing the Application

### 1. Access the Application

Open your browser and navigate to: `http://localhost:4200`

### 2. Default User Accounts

Check the database backup file (`lms_db_backup.sql`) for default test users:

```sql
mysql lms_db < lms_db_backup.sql
```

### 3. API Testing

**Using curl:**
```bash
# Register new user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "name": "Test User",
    "password": "password123"
  }'

# Login
curl -X POST http://localhost:8080/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Using Postman:**
1. Download Postman from https://www.postman.com/downloads/
2. Import the API collection
3. Set environment variable: `baseUrl` = `http://localhost:8080`

## Troubleshooting

### Backend Issues

**Issue: Maven build fails**
```bash
# Clear Maven cache
mvn clean
rm -rf ~/.m2/repository

# Rebuild
mvn clean install
```

**Issue: Java version mismatch**
```bash
# Verify Java 21 is active
java -version
# Output should show: "openjdk version "21.x.x""
```

**Issue: Database connection refused**
```bash
# Start MySQL service
# Windows: net start MySQL80 (or your version)
# macOS: brew services start mysql
# Linux: sudo systemctl start mysql
```

### Frontend Issues

**Issue: Port 4200 already in use**
```bash
# Use different port
ng serve --port 4300
```

**Issue: Module not found errors**
```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

**Issue: Build fails with TypeScript errors**
```bash
# Check TypeScript version
ng version

# Rebuild with verbose output
ng build --verbose
```

## Security Configuration

### Before Deployment

1. **Backend Secrets:**
   - Update `APP_JWT_SECRET` with a strong 32+ character key
   - Update email credentials with production email
   - Update database credentials
   - Set `GEMINI_API_KEY` if using chatbot feature

2. **Frontend Configuration:**
   - Update `environment.prod.ts` with production API URL
   - Ensure `production: true` in prod environment
   - Disable debug logging in production

3. **Database:**
   - Create dedicated database user with limited permissions
   - Enable database backups
   - Use strong passwords

4. **CORS:**
   - Update `WebSecurityConfiguration.java` to allow only production domains
   - Remove `localhost` origins in production

## Development Best Practices

### Backend
- Run tests: `mvn test`
- Check code quality: `mvn checkstyle:check`
- Create feature branches: `git checkout -b feature/your-feature`
- Write meaningful commit messages

### Frontend
- Run tests: `ng test`
- Build for production: `ng build --configuration production`
- Check bundle size: `ng build --stats-json && npm install -g webpack-bundle-analyzer && webpack-bundle-analyzer dist/lms-frontend/stats.json`
- Use ESLint: `ng lint`

## Next Steps

1. Read `IMPROVEMENTS.md` for enhancement recommendations
2. Set up Git hooks for pre-commit checks
3. Configure CI/CD pipeline (GitHub Actions, GitLab CI, etc.)
4. Set up error tracking (Sentry)
5. Configure monitoring and logging
6. Plan database backup strategy

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Angular Documentation](https://angular.io/docs)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [JWT Documentation](https://jwt.io/)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/)

## Support

For issues or questions:
1. Check existing issues on GitHub
2. Review API documentation at `http://localhost:8080/swagger-ui.html`
3. Check application logs in `target/` (backend) or browser console (frontend)
4. Review error messages in Toastr notifications (frontend)
