# Feedback Application

This is a simple feedback management system built with Spring Boot, JPA, PostgreSQL, and Flyway for database migrations. It provides REST APIs to manage users, feedback entries, and permissions.

## Quick Start

### Using Maven

#### 1. Run with default dev profile (without packaging):

```bash
# Using installed Maven
mvn spring-boot:run

# Using Maven Wrapper
./mvnw spring-boot:run
```

#### 2. Package the application and run:

```bash
# Build and run with tests
mvn clean package
java -jar target/app.jar

# Build and run skipping tests
mvn clean package -DskipTests
java -jar target/app.jar

# Using Maven Wrapper
./mvnw clean package -DskipTests
java -jar target/app.jar
```

### Using Docker Compose

The project includes a `docker-compose.yml` that runs the app and a PostgreSQL database.

```bash
docker compose up -d --build
```

This will start:

* `feedback-app` container (Spring Boot application)
* `feedback-postgres` container (PostgreSQL database)

#### Stopping containers:

```bash
docker compose down
```

### Profiles

* **dev**: Default profile for local development. Uses H2 in-memory DB.
* **prod**: Production profile. Uses PostgreSQL. Configure database credentials in `application-prod.yml` or via environment variables.

### Environment Variables

* `DB_USERNAME` and `DB_PASSWORD` for PostgreSQL credentials.
* `ADMIN_INITIAL_PASSWORD` and `ADMIN_INITIAL_EMAIL` for initial admin user.

### Database Migrations

Flyway handles database migrations. Place SQL scripts in `src/main/resources/db/migration`. On startup, Flyway automatically applies any pending migrations.

### Running Tests

Run all tests using:

```bash
mvn test
```

or with the Maven wrapper:

```bash
./mvnw test
```

## Usage

* REST API available at `http://localhost:8080`
* Admin user can manage feedback and users.
* Default dev credentials:

    * Email: `admin@feedback.com`
    * Password: `admin`

## Notes

* Docker setup is convenient for testing with PostgreSQL.
* For production, configure SMTP and database credentials via environment variables.
* Use `prod` profile for connecting to real PostgreSQL and sending emails.

---

*Enjoy using Feedback Application!*
