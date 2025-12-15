# PlanDecks

A Spring Boot REST API application providing user authentication and authorization services using JWT (JSON Web Tokens).

## Overview

PlanDecks is a secure authentication system built with Spring Boot that offers user registration, login, logout, password reset, and account management functionality. The application uses JWT tokens for stateless authentication and Redis for token blacklisting.

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Security** - For authentication and authorization
- **Spring Data JPA** - For database operations
- **PostgreSQL** - Primary database
- **Redis** - Token blacklist management
- **JWT (JSON Web Tokens)** - For secure authentication
- **Lombok** - To reduce boilerplate code
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Build and dependency management

## Prerequisites

Before running this application, ensure you have the following installed:

- Java 21 or higher
- Maven 3.6+
- PostgreSQL database
- Redis server

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/emreakburakci/plandecks.git
   cd plandecks
   ```

2. **Set up PostgreSQL database:**
   ```sql
   CREATE DATABASE plandecks;
   ```

3. **Configure database connection:**
   
   Update the `src/main/resources/application.properties` file with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/plandecks
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Start Redis server:**
   ```bash
   redis-server
   ```
   
   Default configuration expects Redis running on `localhost:6379`

5. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

## Running the Application

Run the application using Maven:

```bash
./mvnw spring-boot:run
```

Or run the JAR file after building:

```bash
java -jar target/plandecks-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## Configuration

Key configuration properties in `application.properties`:

```properties
# Application name
spring.application.name=plandecks

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/plandecks
spring.datasource.username=postgres
spring.datasource.password=POSTGRES

# JPA/Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Redis configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=60000
```

## API Endpoints

### Authentication Endpoints

All authentication endpoints are prefixed with `/api/auth`

#### 1. Register
- **URL:** `POST /api/auth/register`
- **Description:** Register a new user account
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "securePassword123"
  }
  ```
- **Response:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

#### 2. Login
- **URL:** `POST /api/auth/login`
- **Description:** Authenticate an existing user
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "securePassword123"
  }
  ```
- **Response:**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

#### 3. Logout
- **URL:** `POST /api/auth/logout`
- **Description:** Logout user and blacklist the current token
- **Headers:**
  ```
  Authorization: Bearer <your_jwt_token>
  ```
- **Response:** `"Logout successful"`

#### 4. Reset Password
- **URL:** `POST /api/auth/reset-password`
- **Description:** Reset user password
- **Query Parameters:**
  - `email` - User's email address
  - `newPassword` - New password
- **Response:** `"Password reset successful"`
- **Note:** Email verification to be added in future updates

#### 5. Delete Account
- **URL:** `DELETE /api/auth`
- **Description:** Delete user account
- **Query Parameters:**
  - `email` - User's email address
- **Response:** `"Account deleted successfully"`
- **Note:** Email verification to be added in future updates

#### 6. Test Authentication
- **URL:** `GET /api/auth/test`
- **Description:** Test endpoint to verify authentication
- **Headers:**
  ```
  Authorization: Bearer <your_jwt_token>
  ```
- **Response:** `"You are authenticated"`

## API Documentation

Once the application is running, you can access the interactive API documentation (Swagger UI) at:

```
http://localhost:8080/swagger-ui.html
```

This provides a user-friendly interface to explore and test all available endpoints.

## Security Features

- **JWT Authentication:** Stateless authentication using JSON Web Tokens
- **Password Encryption:** User passwords are encrypted using BCrypt
- **Token Blacklisting:** Invalidated tokens are stored in Redis to prevent reuse
- **Bearer Token Authentication:** Secure API access using Authorization headers
- **Scheduled Cleanup:** Automatic cleanup of expired user logs

## Project Structure

```
plandecks/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── plandecks/
│   │   │           ├── PlandecksApplication.java
│   │   │           └── auth/
│   │   │               ├── AuthController.java
│   │   │               ├── AuthService.java
│   │   │               ├── User.java
│   │   │               ├── UserRepository.java
│   │   │               ├── UserLog.java
│   │   │               ├── UserLogRepository.java
│   │   │               ├── JwtUtil.java
│   │   │               ├── JwtAuthFilter.java
│   │   │               ├── SecurityConfig.java
│   │   │               ├── RedisConfig.java
│   │   │               ├── TokenBlacklistService.java
│   │   │               ├── OpenApiConfig.java
│   │   │               ├── SchedulerConfig.java
│   │   │               ├── LoginRequest.java
│   │   │               ├── RegisterRequest.java
│   │   │               └── AuthResponse.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Future Improvements

- Email verification for account operations (registration, password reset, account deletion)
- Enhanced password reset flow with token-based verification
- Rate limiting for authentication endpoints
- OAuth2 integration for social login
- User profile management
- Role-based access control (RBAC)

## License

This project is currently unlicensed. Please contact the repository owner for licensing information.

## Author

**Emre Akburakçı**

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
