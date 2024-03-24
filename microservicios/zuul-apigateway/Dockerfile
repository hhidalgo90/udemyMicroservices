FROM openjdk:17
VOLUME /tmp
EXPOSE 8090
ADD ./target/zuul-apigateway-0.0.1-SNAPSHOT.jar zuul-server.jar
ENTRYPOINT ["java", "-jar", "/zuul-server.jar"]