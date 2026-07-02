# Library Management System — Week 5: Integration, Deployment & Documentation

A menu-driven Java CLI application for managing a library's book inventory,
now consolidated into a single Maven project with CSV persistence, logging,
JUnit tests, and Docker packaging.

This is the **Week 5 (final)** milestone of the YuvaIntern Java Development
internship. It integrates the CLI (Week 2), unit tests (Week 3), and
design-pattern refactor (Week 4) into one deployable application.

## Quick Start

```bash
# Build
mvn clean package

# Run
java -jar target/library-management-system.jar
```

Or use the helper script:

```bash
./deploy/build-and-run.sh
```

## Run with Docker

```bash
./deploy/docker-deploy.sh
```

This builds a multi-stage Docker image (Maven build stage → slim JRE
runtime stage) and runs the CLI interactively, with `data/` and `logs/`
mounted from the host so records persist across container runs.

## Project Structure

```
library-app/
├── pom.xml
├── Dockerfile
├── .dockerignore
├── deploy/
│   ├── build-and-run.sh
│   └── docker-deploy.sh
├── data/                # books.csv created at runtime
├── logs/                # app.log created at runtime
└── src/
    ├── main/java/com/raunak/library/
    │   ├── LibraryApp.java              # CLI entry point
    │   ├── model/Book.java
    │   ├── exception/                   # custom checked exceptions
    │   ├── repository/                  # Repository pattern (CSV persistence)
    │   ├── service/                     # LibraryService, Factory & Strategy patterns
    │   └── util/AppLogger.java
    └── test/java/com/raunak/library/
        └── LibraryServiceTest.java      # JUnit 5 tests
```

See **DEVELOPER_DOCUMENTATION.docx** for full architecture, deployment, and
maintenance notes.

## Author

Raunak Mishra — YuvaIntern Java Development Internship, Week 5
