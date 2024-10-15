FROM maven:3.9.6-amazoncorretto-21 as builder
WORKDIR application
COPY . /application/
COPY ../pom.xml /application/ewm-stats-parent.xml
COPY ../../pom.xml /application/ewm-parent.xml
ENV EWM_STATS_PARENT_POM=../ewm-stats-parent.xml
ENV EWM_PARENT_POM=../ewm-parent.xml
RUN mvn -f /application/pom.xml clean package -DskipTests=true

FROM amazoncorretto:21-alpine
WORKDIR application
COPY --from=builder /application/target/explore-with-me-statistics-api.jar /application/api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]