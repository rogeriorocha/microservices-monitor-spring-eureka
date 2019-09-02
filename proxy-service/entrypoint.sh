#!/bin/sh

while ! nc -z config-server 8088 ; do
    echo "Waiting for upcoming Config Server"
    sleep 2
done


while ! nc -z discovery-service 8061 ; do
    echo "Waiting for upcoming Discovery Service"
    sleep 2
done

java -jar -Djava.security.egd=file:/dev/./urandom /app/proxy-service-1.0-SNAPSHOT.jar

