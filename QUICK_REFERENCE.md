# Quick Reference Guide

## Getting Started (5 minutes)

### Backend
```bash
cd lms-backend
cp .env.example .env
# Edit .env with your database credentials
mvn clean install
mvn spring-boot:run
# Available at http://localhost:8080
```

### Frontend
```bash
cd lms-frontend
npm install
ng serve
# Available at http://localhost:4200
```

## Project Structure

### Backend (`lms-backend/`)
```
src/main/java/com/ibizabroker/lms/
├── controller/          # REST endpoints
├── service/             # Business logic
├── entity/              # Database models
├── dto/                 # Data transfer objects (with validation)
├── dao/                 # Repository interfaces
├── configuration/       # Spring configurations
├── exceptions/          # Custom exceptions
└── util/                # Utilities (JWT, etc.)

src/main/resources/
├── application.properties    # Configuration (use env vars!)
└── data.sql                  # Initial data
```

### Frontend (`lms-frontend/`)
```
src/app/
├── services/            # HTTP calls & business logic
│   ├── api.service.ts              # ✨ NEW: Centralized API URLs
│   ├── loading.service.ts          # ✨ NEW: Loading state
│   ├── logger.service.ts           # ✨ NEW: Logging
│   ├── user-auth.service.ts        # Auth & token management
│   └── [other services]
├── auth/
│   ├── auth.interceptor.ts         # JWT token injection
│   ├── error.interceptor.ts        # ✨ NEW: Global error handling
│   ├── loading.interceptor.ts      # ✨ NEW: Loading indicator
│   └── auth.guard.ts               # Route protection
├── [feature components]            # UI components
└── models/                         # TypeScript interfaces
```

## Key Patterns

### Using API Service (Recommended)
```typescript
import { ApiService } from '@app/services/api.service';

constructor(private api: ApiService, private http: HttpClient) {}

getBooks() {
  const url = this.api.getBooksEndpoints().list;
  return this.http.get(url);
}

deleteBook(id: number) {
  const url = this.api.getBooksEndpoints().delete(id);
  return this.http.delete(url);
}
```

### Using Loading Service
```typescript
import { LoadingService } from '@app/services/loading.service';

constructor(private loading: LoadingService) {}

get loading$() {
  return this.loading.loading$;
}

// In template:
// <div *ngIf="loading$ | async" class="spinner"></div>
```

### Using Logger Service
```typescript
import { LoggerService } from '@app/services/logger.service';

constructor(private logger: LoggerService) {}

doSomething() {
  this.logger.info('User clicked button');
  this.logger.debug('User data:', userData);
  
  try {
    // do work
  } catch (e) {
    this.logger.error('Operation failed', e);
  }
}
```

### Backend Validation
```java
// In DTO
@NotEmpty(message = "Name cannot be empty")
@Size(min = 1, max = 255)
private String name;

@NotNull(message = "Quantity required")
@Min(value = 1, message = "Must be positive")
private Integer quantity;

// In Controller
@PostMapping
public ResponseEntity<Book> create(@Valid @RequestBody BookCreateDto dto) {
    // @Valid triggers validation automatically
    return ResponseEntity.ok(bookService.create(dto));
}

// If validation fails → ApiExceptionHandler returns 400 with details
// {"message": "Dữ liệu không hợp lệ", "errors": {"name": "Name cannot be empty"}}
```

## API Endpoints Reference

### Authentication
```
POST   /auth/register          Create new user
POST   /auth/authenticate      Login, get JWT token
```

### Books (Admin Only)
```
GET    /admin/books/{id}       Get single book
POST   /admin/books            Create book
PUT    /admin/books/{id}       Update book
DELETE /admin/books/{id}       Delete book
GET    /admin/books/authors    List all authors
GET    /admin/books/categories List all categories
```

### Books (Public)
```
GET    /public/books           List all books
GET    /public/books?search=...  Search books
```

### Circulation (Users)
```
POST   /user/borrow            Borrow book
POST   /user/return            Return book
GET    /user/my-borrows        User's borrow history
```

## Common Tasks

### Add New API Endpoint
```java
// 1. Create DTO with validation
@Data
public class NewThingDto {
    @NotBlank(message = "Name required")
    private String name;
}

// 2. Add to service
@Service
public class ThingService {
    public Thing create(NewThingDto dto) {
        // business logic
    }
}

// 3. Add to controller
@RestController
@RequestMapping("/admin/things")
public class ThingController {
    @PostMapping
    public Thing create(@Valid @RequestBody NewThingDto dto) {
        return service.create(dto);
    }
}

// 4. Add to ApiService (frontend)
getThingyEndpoints() {
    return {
        list: this.buildUrl('/admin/things'),
        create: this.buildUrl('/admin/things'),
    };
}

// 5. Use in component
getThings() {
    const url = this.api.getThingyEndpoints().list;
    return this.http.get(url);
}
```

### Implement OnDestroy (Prevent Memory Leaks)
```typescript
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({...})
export class MyComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  constructor(private service: MyService) {}

  ngOnInit() {
    this.service.getData()
      .pipe(takeUntil(this.destroy$))
      .subscribe(data => {
        // use data
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

### Add Form Validation Feedback
```html
<form [formGroup]="form">
  <input 
    formControlName="name"
    [ngClass]="{'is-invalid': form.get('name')?.invalid && form.get('name')?.touched}">
  
  <div class="invalid-feedback" *ngIf="form.get('name')?.invalid && form.get('name')?.touched">
    <span *ngIf="form.get('name')?.errors?.['required']">Name is required</span>
    <span *ngIf="form.get('name')?.errors?.['minlength']">Minimum 3 characters</span>
  </div>
  
  <button [disabled]="form.invalid">Submit</button>
</form>
```

## Troubleshooting

### Port Already in Use
```bash
# Frontend on different port
ng serve --port 4300

# Backend on different port (update application.properties)
server.port=8081
```

### Database Connection Fails
```bash
# Check MySQL is running
# Windows: net start MySQL80
# macOS: brew services start mysql
# Linux: sudo systemctl start mysql

# Check credentials in .env file
# Verify database exists
mysql -u root -p lms_db
```

### "Cannot find symbol" Error
```bash
# Backend: Clean and rebuild
cd lms-backend
mvn clean
mvn compile

# Check Java 21 is installed
java -version
```

### Frontend Module Not Found
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install

# Check Angular CLI version
ng version
```

## Performance Tips

### Backend
- Use `@Transactional(readOnly = true)` for queries
- Implement pagination for large lists
- Use database indexes on frequently queried columns
- Cache repeated queries

### Frontend
- Implement `OnDestroy` to prevent memory leaks
- Use `OnPush` change detection strategy
- Lazy load modules with routing
- Unsubscribe from observables using `takeUntil`

## Security Checklist

- ✅ JWT token stored securely (in secure HTTP-only cookies ideal)
- ✅ Passwords hashed with BCrypt
- ✅ CORS configured properly
- ✅ Environment variables for secrets (NOT committed)
- ✅ Error messages don't leak sensitive info
- ❌ HTTPS not enforced (add to production)
- ❌ SQL injection not possible (using JPA)
- ❌ XSS not possible (Angular escapes by default)

## Useful Commands

### Backend
```bash
# Build and test
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Check code quality
mvn checkstyle:check

# Generate JAR for deployment
mvn clean package -DskipTests
```

### Frontend
```bash
# Install dependencies
npm install

# Start dev server
ng serve

# Build for production
ng build --configuration production

# Run tests
ng test

# Lint code
ng lint

# Check bundle size
ng build --stats-json
webpack-bundle-analyzer dist/lms-frontend/stats.json
```

### Database
```bash
# Backup database
mysqldump -u root -p lms_db > backup.sql

# Restore database
mysql -u root -p lms_db < backup.sql

# Connect to MySQL
mysql -u root -p

# View schema
DESCRIBE books;
```

## Environment Variables (Backend)

```env
# Required
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/lms_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=
APP_JWT_SECRET=minimum-32-characters-secret-key-here
APP_JWT_EXPIRATION=86400000

# Optional
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=email@gmail.com
SPRING_MAIL_PASSWORD=password
GEMINI_API_KEY=your-key
SERVER_PORT=8080
```

## Important Files

| File | Purpose |
|------|---------|
| `.env.example` | Environment variable template |
| `SETUP_GUIDE.md` | Complete setup instructions |
| `IMPROVEMENTS.md` | Enhancement roadmap |
| `REVIEW_SUMMARY.md` | Review findings summary |
| `pom.xml` | Backend dependencies |
| `package.json` | Frontend dependencies |
| `environment.ts` | Dev environment config |
| `environment.prod.ts` | Production environment config |

## Contact & Support

- Check `SETUP_GUIDE.md` for detailed instructions
- Review `IMPROVEMENTS.md` for future enhancements
- See `REVIEW_SUMMARY.md` for all changes made
- Consult API endpoints in `README.md`

---

**Last Updated**: November 2025
**Version**: 1.0 (Post-Review Improvements)
