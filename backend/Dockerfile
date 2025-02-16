FROM openjdk:23

WORKDIR /app

COPY ./gradle ./gradle
COPY ./gradlew .
COPY ./build.gradle .
COPY ./settings.gradle .
COPY ./build/libs/*.jar app.jar

# Expose the application port
EXPOSE 3000

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
