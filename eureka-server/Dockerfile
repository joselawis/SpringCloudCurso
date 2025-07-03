# Dockerfile independiente para eureka-server
FROM maven:3.9.4-amazoncorretto-21 AS builder

WORKDIR /app

# Copiar el proyecto del eureka-server
COPY eureka-server/pom.xml .
COPY eureka-server/src src

# Compilar el microservicio
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

EXPOSE 8761

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

ENTRYPOINT [ "java", "-jar", "eureka-server.jar" ]