#!/bin/sh

echo "Waiting for Config Server to be ready..."

while ! curl -s http://config-server:8080/actuator/health | grep '"status":"UP"' > /dev/null; do
  echo "Config Server not ready yet..."
  sleep 5
done

echo "Config Server is UP. Starting Category service..."

# Run your actual app here (replace with the real command)
exec java -jar /app/authentication.jar
