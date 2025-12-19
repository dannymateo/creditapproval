# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy Maven configuration files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Compile and package application
RUN mvn clean package -DskipTests

# Final image stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Default environment variables
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Start command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

