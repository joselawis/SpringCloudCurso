@echo off
echo ============================================
echo Spring Cloud Microservices - Docker
echo ============================================
echo.

echo Building all services...
docker-compose build

echo.
echo Starting all services (with Zipkin via override)...
docker-compose up -d

echo.
echo ============================================
echo Services started successfully!
echo ============================================
echo.
echo Available services:
echo - Eureka Server: http://localhost:8761
echo - Config Server: http://localhost:8888
echo - Gateway: http://localhost:8090
echo - Zipkin: http://localhost:9411
echo - MySQL: localhost:3307
echo.
echo To view logs: docker-compose logs -f [service-name]
echo To stop: docker-compose down
echo.
pause
