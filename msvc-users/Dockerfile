FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

EXPOSE 8080
COPY ./target/msvc-users-0.0.1-SNAPSHOT.jar msvc-users.jar

ENTRYPOINT [ "java", "-jar", "msvc-users.jar" ]
