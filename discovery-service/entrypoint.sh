#!/bin/sh

while ! nc -z config-server 8088 ; do
    echo "Waiting for upcoming Config Server"
    sleep 2
done

java -jar -Djava.security.egd=file:/dev/./urandom /app/discovery-service-1.0-SNAPSHOT.jar

