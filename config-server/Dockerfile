# Dockerfile independiente para config-server
FROM maven:3.9.4-amazoncorretto-21 AS builder

WORKDIR /app

# Copiar el proyecto del config-server
COPY config-server/pom.xml .
COPY config-server/src src

# Compilar el microservicio
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

EXPOSE 8888

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/config-server-0.0.1-SNAPSHOT.jar config-server.jar

ENTRYPOINT [ "java", "-jar", "config-server.jar" ]