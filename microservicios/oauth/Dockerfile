FROM openjdk:17
VOLUME /tmp
EXPOSE 9100
ADD ./target/oauth-0.0.1-SNAPSHOT.jar oauth.jar
ENTRYPOINT ["java", "-jar", "/oauth.jar"]