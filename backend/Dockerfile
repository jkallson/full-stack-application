FROM openjdk:23

WORKDIR /app
COPY ./build/libs/*.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]
