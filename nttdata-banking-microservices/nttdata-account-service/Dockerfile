# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build
WORKDIR /build

# Install parent POM first
COPY pom.xml .
RUN mvn -N install

# First, build and install shared library
COPY nttdata-shared-library/pom.xml nttdata-shared-library/
COPY nttdata-shared-library/src nttdata-shared-library/src
RUN mvn -f nttdata-shared-library/pom.xml clean install -DskipTests

# Now build account service
COPY nttdata-account-service/pom.xml nttdata-account-service/
COPY nttdata-account-service/src nttdata-account-service/src
RUN mvn -f nttdata-account-service/pom.xml clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy JAR from build stage
COPY --from=build /build/nttdata-account-service/target/*.jar app.jar

# Expose port
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Set active profile (can be overridden)
ENV SPRING_PROFILES_ACTIVE=prod

# JVM options for container environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
