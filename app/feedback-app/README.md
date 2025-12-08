# Feedback App

Simple Spring Boot + Maven application for collecting feedback.

## Features
- Public feedback form (name, email, contact type, message)
- Admin page to list feedbacks, filter by contact type and sort by date.
- Authentication for /admin via HTTP Basic (default admin/adminpass).
- JPA (H2 in dev, Postgres profile for prod).
- Dockerfile + docker-compose for easy run.

## Run locally (dev)
Requirements: Java 17, Maven

```bash
# build
mvn clean package

# run (H2 in-memory)
java -jar target/feedback-app-0.0.1-SNAPSHOT.jar
# open http://localhost:8080
# admin: http://localhost:8080/admin/list (user=admin / pass=adminpass)
```

## Run with Docker (Postgres)
```bash
docker compose up --build
# waits for postgres; app runs with 'prod' profile and expects db credentials in docker-compose.yml
```

## API Endpoints
- `GET /` - Feedback form
- `POST /submit` - Submit feedback
- `GET /admin/list` - List feedbacks (requires auth)

## Notes on production readiness
- Add centralized logging (ELK / Grafana Loki), externalized secrets for DB credentials (Vault / Kubernetes secrets).
- Replace in-memory user store with a proper user service (LDAP, Keycloak, OAuth2).
- Add request validation and rate limiting.
- Add monitoring (Actuator, Prometheus).
- Harden security (CSRF, CSP headers) and enable HTTPS.

