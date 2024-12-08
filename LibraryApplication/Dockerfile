FROM openjdk:21-jdk-alpine

WORKDIR /app

COPY build/libs/LibraryApplication-0.0.1-SNAPSHOT.jar.jar /app/libraryApplication.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "libraryApplication.jar"]

MAINTAINER "Kamran Aghalarli"