FROM amazoncorretto:21-alpine-jdk

COPY target/spring-1.0.jar app.jar

ENTRYPOINT ["java" , "-jar" , "/app.jar"]
