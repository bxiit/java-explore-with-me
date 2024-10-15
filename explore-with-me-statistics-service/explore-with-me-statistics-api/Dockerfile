FROM maven as stage1
WORKDIR /application
COPY . /application/
COPY ../pom.xml /application/ewm-stats-parent.xml
COPY ../../pom.xml /application/ewm-parent.xml
ENV EWM_STATS_PARENT_POM=../ewm-stats-parent.xml
ENV EWM_PARENT_POM=../ewm-parent.xml

FROM amazoncorretto:21-alpine
WORKDIR application
COPY --from=stage1 /application/target/explore-with-me-statistics-api.jar /application/api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]