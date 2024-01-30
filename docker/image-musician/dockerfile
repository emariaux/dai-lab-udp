FROM maven:3-eclipse-temurin-21-alpine
WORKDIR /app
COPY src /app/src
COPY pom.xml /app/pom.xml
RUN mvn clean compile assembly:single
RUN mv /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]