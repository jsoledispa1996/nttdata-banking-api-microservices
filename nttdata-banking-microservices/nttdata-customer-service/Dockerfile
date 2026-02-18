# Multi-stage build for Customer Service
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

# Install parent POM first
COPY pom.xml .
RUN mvn -N install

# First, build and install shared library
COPY nttdata-shared-library/pom.xml nttdata-shared-library/
COPY nttdata-shared-library/src nttdata-shared-library/src
RUN mvn -f nttdata-shared-library/pom.xml clean install -DskipTests

# Now build customer service
COPY nttdata-customer-service/pom.xml nttdata-customer-service/
COPY nttdata-customer-service/src nttdata-customer-service/src
RUN mvn -f nttdata-customer-service/pom.xml clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from build stage
COPY --from=build /build/nttdata-customer-service/target/nttdata-customer-service-*.jar app.jar

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --quiet --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
