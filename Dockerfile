# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first (better layer caching)
COPY pom.xml .
RUN mvn dependency:resolve

# Copy sources
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# JVM options (can be overridden)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]