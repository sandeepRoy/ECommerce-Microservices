# Use an OpenJDK base image
FROM openjdk:17

WORKDIR /app

COPY target/Config-Server-0.0.1-SNAPSHOT.jar config-server.jar

COPY wait-for-mysql.sh /app/

RUN chmod +x /app/wait-for-mysql.sh

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "config-server.jar"]
