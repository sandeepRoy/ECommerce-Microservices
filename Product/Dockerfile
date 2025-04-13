FROM openjdk:17
WORKDIR /app

COPY target/Product-0.0.1-SNAPSHOT.jar /app/product.jar
COPY wait-for-config.sh /app/wait-for-config.sh

RUN chmod +x /app/wait-for-config.sh

ENTRYPOINT ["/app/wait-for-config.sh"]
