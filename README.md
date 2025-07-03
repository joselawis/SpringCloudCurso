# Spring Cloud Microservices

## Estructura del Proyecto

- **config-server**: Servidor de configuración
- **eureka-server**: Servidor de registro y descubrimiento
- **msvc-gateway-server**: Gateway API
- **msvc-users**: Microservicio de usuarios
- **msvc-products**: Microservicio de productos
- **msvc-items**: Microservicio de items (compuesto)
- **msvc-oauth**: Servidor de autenticación OAuth2
- **libs-msvc-commons**: Librería compartida común
- **libs-msvc-entities**: Entidades compartidas

## Scripts Disponibles

### Para Desarrollo Local (IDE)
```bash
# Construir todos los servicios
build-all.bat

# Iniciar solo infraestructura (MySQL, Config Server, Eureka)
start-local-dev.bat
```

### Para Docker
```bash
# Iniciar todo el stack en Docker (incluye Zipkin)
start-docker.bat

# O manualmente
docker-compose up -d
```

## Configuración

- **`.env`**: Variables para desarrollo local (IDE)
- **`docker-compose.yml`**: Stack principal
- **`docker-compose.override.yml`**: Agrega Zipkin automáticamente
- **`docker-compose-zipkin.yml`**: Zipkin standalone con Cassandra (ejemplo)
- **`docker-compose-sonarqube.yml`**: SonarQube (ejemplo para futuro)

## URLs de Servicios

### Desarrollo Local
- Eureka: http://localhost:8761
- Config Server: http://localhost:8888
- Gateway: http://localhost:8090
- MySQL: localhost:3307

### Con Zipkin
- Zipkin UI: http://localhost:9411

## Notas
- El archivo `docker-compose.override.yml` se aplica automáticamente y agrega Zipkin
- Para desarrollo local, usar `start-local-dev.bat` y luego iniciar servicios desde el IDE
- Para Docker completo, usar `start-docker.bat`
