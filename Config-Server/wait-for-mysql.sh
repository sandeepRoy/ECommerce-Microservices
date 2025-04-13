#!/bin/sh

echo "Waiting for MySQL to be ready..."

while ! mysqladmin ping -h"mysql" -u"root" -p"root" --silent; do
  echo "MySQL is unavailable - sleeping"
  sleep 5
done

echo "MySQL is up - starting Config-Server"
exec java -jar /app/config-server.jar
