FROM openjdk:8-jre-alpine
ENV APP_FILE auth-service-1.0.0-SNAPSHOT.jar
ENV APP_HOME /app
EXPOSE 8061
COPY target/$APP_FILE $APP_HOME/
COPY entrypoint.sh /$APP_HOME
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
RUN chmod 755 $APP_HOME/entrypoint.sh

#CMD ["exec java -jar -Djava.security.egd=file:/dev/./urandom $APP_FILE"]
#discovery-service-1.0-SNAPSHOT.jar

