FROM amazoncorretto:21-alpine
WORKDIR application
COPY ./target/*.jar /application/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]