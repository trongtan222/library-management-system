# Test Credentials

## Default Test Users

The following test users have been added to the database and are ready to use:

### Admin Account
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: admin@lms.com
- **Role**: ROLE_ADMIN
- **Full Name**: Administrator

### Regular User Account
- **Username**: `user`
- **Password**: `user123`
- **Email**: user@lms.com
- **Role**: ROLE_USER
- **Full Name**: Test User

## How to Login

### Via Frontend (Angular)
1. Go to `http://localhost:4200`
2. Click "Login"
3. Enter username and password from above
4. Click "Sign In"

### Via API (cURL)
```bash
curl -X POST http://localhost:8080/api/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

You'll receive a JWT token in response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

## Using JWT Token

Include the token in the Authorization header for authenticated requests:

```bash
curl -X GET http://localhost:8080/api/user/circulation \
  -H "Authorization: Bearer {token_here}"
```

## Database Information

- **Database**: lms_db
- **Host**: localhost:3306
- **Default User**: root
- **Password**: (empty)

**Note**: These are test credentials for development only. Change them in production!

## Password Hashes

Both passwords are BCrypt hashed with the spring boot BCryptPasswordEncoder:
- `admin123` → `$2a$10$aBTrz8BLjLnjvl7cEtPaJeWz0pDp5NRRRXLHhKkGUEjXL8jTKNmky`
- `user123` → `$2a$10$6Y7JgBmEUVh3fC8UzL9dveVhDhHW8bqQbRkHXJ7gGm5x9E0qW6Pry`

## What You Can Do

### As Admin
- Access admin dashboard
- Manage users
- View all library statistics
- Create/edit/delete books
- Manage loans and reservations
- View fines and overdue items
- Generate reports

### As User
- Browse books
- Borrow books (circulation)
- Make reservations
- View borrowing history
- Chat with library assistant (RAG chatbot)
- Leave reviews and ratings

## Endpoints to Test

### Public (No Auth Required)
```
GET /api/public/books - Get all books
GET /api/public/books/{id} - Get book details
GET /api/public/books/search?query=title - Search books
```

### Admin Only
```
GET /api/admin/dashboard - View dashboard
GET /api/admin/users - Manage users
GET /api/admin/loans - View all loans
GET /api/admin/reports - Generate reports
```

### User Only
```
GET /api/user/circulation - View borrowing history
POST /api/user/circulation/borrow - Borrow a book
POST /api/user/circulation/reserve - Reserve a book
GET /api/user/chat - Access chatbot
```

---

**Status**: ✅ Ready to test!

Start the backend with `mvn spring-boot:run` and frontend with `npm start`, then login with these credentials.
