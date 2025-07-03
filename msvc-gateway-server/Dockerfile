# Dockerfile independiente para msvc-gateway-server
FROM maven:3.9.4-amazoncorretto-21 AS builder

WORKDIR /app

# Copiar las librerías compartidas primero (para cache de Docker)
COPY libs-pom.xml /workspace/libs-pom.xml
COPY libs-msvc-commons /workspace/libs-msvc-commons
COPY libs-msvc-entities /workspace/libs-msvc-entities

# Compilar e instalar las librerías en el repositorio local
WORKDIR /workspace
RUN mvn clean install -f libs-pom.xml -DskipTests

# Ahora trabajar con el microservicio
WORKDIR /app
COPY msvc-gateway-server/pom.xml .
COPY msvc-gateway-server/src src

# Compilar el microservicio
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

EXPOSE 8090

# Copiar el JAR compilado desde la etapa anterior
COPY --from=builder /app/target/msvc-gateway-server-0.0.1-SNAPSHOT.jar msvc-gateway-server.jar

ENTRYPOINT [ "java", "-jar", "msvc-gateway-server.jar" ]
