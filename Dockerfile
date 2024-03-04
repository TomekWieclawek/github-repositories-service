FROM eclipse-temurin:21-jdk-alpine

COPY target/*.jar repositoryApp.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/repositoryApp.jar"]