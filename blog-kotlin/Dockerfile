FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle shadowJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create logs directory
RUN mkdir -p logs

# Copy the JAR from build stage
COPY --from=build /app/build/libs/blog-kotlin-1.0.0-all.jar app.jar

# Expose the application port
EXPOSE 7070

# Run the application
CMD ["java", "-jar", "app.jar"]
