# How to Run & Test the Solution

Backend API for managing retail master data (Provinces → Branches → Stores)
with JWT authentication, soft delete, and full audit trail.

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Security + JWT
- Spring Data JPA + MySQL 8
- Maven

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+

## Setup

### 1. Create database

```sql
CREATE DATABASE retail_dev;
```

### 2. Configure environment

```bash
cp .env.example .env
```

Edit `.env`:

```dotenv
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

DB_HOST=localhost
DB_PORT=3306
DB_NAME=retail_dev
DB_USERNAME=root
DB_PASSWORD=yourpassword

JWT_SECRET=your-secret-at-least-32-characters-long
```

> `JWT_SECRET` must be at least 32 characters. Generate one:
> `openssl rand -base64 64`

### 3. Build & run

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

App runs at `http://localhost:8080/api/v1`.

On first start (dev profile only), an admin user is bootstrapped:

- Username: `admin`
- Password: `admin123`

> Change these in `.env` before running elsewhere:
> `ADMIN_USERNAME`, `ADMIN_PASSWORD`

---

## Test the Solution (Skip to number 4 to use Postman/Bruno)

### 1. Login & get JWT token

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**Response (200)**:

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400,
    "username": "admin",
    "role": "ADMIN"
  }
}
```

### 2. Set token in shell

```bash
export BASE=http://localhost:8080/api/v1
export TOKEN=<paste-accessToken-here>
```

### 3. Smoke test — full chain

```bash
# Create province
curl -X POST $BASE/provinces \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"code":"31","name":"DKI Jakarta"}'

# Create branch
curl -X POST $BASE/branches \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"provinceId":1,"code":"JKT-01","name":"Jakarta Pusat"}'

# Create store
curl -X POST $BASE/stores \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"branchId":1,"code":"STR-001","name":"Indomaret Sudirman","whitelisted":true}'

# search store by province name
curl -H "Authorization: Bearer $TOKEN" \
  "$BASE/stores?provinceName=jakarta"

# list whitelisted stores
curl -H "Authorization: Bearer $TOKEN" \
  "$BASE/stores?whitelistedOnly=true"

# update branch
curl -X PATCH $BASE/branches/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"phone":"021-9999999"}'

# soft delete works (subsequent GET returns 404)
curl -X DELETE $BASE/stores/1 -H "Authorization: Bearer $TOKEN"
curl -i $BASE/stores/1 -H "Authorization: Bearer $TOKEN"   # → 404

# auth required (no token → 401)
curl -i $BASE/provinces
```

### 4. Postman / Bruno

Import the ready-made collection from `docs/`:

- Postman: `docs/postman/Backend_Mart_Anas.json`
- Bruno: open `docs/bruno/` folder in Bruno app

Both include a `Login` request with a script that auto-saves the token
to the environment. All other requests inherit the auth header.

Full API reference: `docs/api-documentation.pdf`

---

## Project Structure

```
src/main/java/com/example/backend_mart/
├── auth/         # POST /auth/login
├── province/     # Province feature (CRUD)
├── branch/       # Branch feature (CRUD, FK → Province)
├── store/        # Store feature (CRUD, FK → Branch, whitelist)
├── audit/        # Audit trail (append-only)
├── security/     # JWT filter, user details
├── config/       # App configuration
└── common/       # Shared: ApiResponse, exceptions, specifications
```

Feature-based packaging - Each domain is independent.

---

## Further Reading

- Database design: `docs/database-design.pdf`
- API reference: `docs/api-documentation.pdf`