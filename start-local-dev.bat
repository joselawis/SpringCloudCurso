@echo off
echo ============================================
echo Spring Cloud - Local Development Setup
echo ============================================
echo.
echo This script starts only the infrastructure services
echo for local development with your IDE.
echo.

echo Starting infrastructure services...
docker-compose up -d mysql config-server eureka-server

echo.
echo ============================================
echo Infrastructure services started!
echo ============================================
echo.
echo Available infrastructure:
echo - MySQL Database: localhost:3307
echo - Config Server: http://localhost:8888
echo - Eureka Server: http://localhost:8761
echo.
echo Now you can start your microservices from your IDE:
echo - msvc-users (recommended port: 8081)
echo - msvc-products (recommended port: 8001)
echo - msvc-items (port: 8002)
echo - msvc-oauth (port: 9100)
echo - msvc-gateway-server (port: 8090)
echo.
echo To start Zipkin separately: docker run -d -p 9411:9411 openzipkin/zipkin
echo To stop infrastructure: docker-compose down
echo.
pause
