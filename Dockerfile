# ---------- Stage 1: Build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Non-root user for safer container execution
RUN useradd -ms /bin/bash appuser
COPY --from=build /build/target/library-management-system.jar ./library-management-system.jar

# Persist data and logs outside the container's writable layer
VOLUME ["/app/data", "/app/logs"]

USER appuser

# CLI app: run with `docker run -it` so stdin is attached
ENTRYPOINT ["java", "-jar", "library-management-system.jar"]
