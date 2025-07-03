@echo off
echo ============================================
echo Building all Spring Cloud microservices
echo ============================================
echo.

echo Cleaning Maven cache for this project...
call mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false -Dsilent=true

echo Building shared libraries...
call mvn clean install -f libs-pom.xml -DskipTests -U

echo Building config-server...
cd config-server
call mvn clean package -DskipTests -U
cd ..

echo Building eureka-server...
cd eureka-server
call mvn clean package -DskipTests -U
cd ..

echo Building msvc-users...
cd msvc-users
call mvn clean package -DskipTests -U
cd ..

echo Building msvc-products...
cd msvc-products
call mvn clean package -DskipTests -U
cd ..

echo Building msvc-items...
cd msvc-items
call mvn clean package -DskipTests -U
cd ..

echo Building msvc-oauth...
cd msvc-oauth
call mvn clean package -DskipTests -U
cd ..

echo Building msvc-gateway-server...
cd msvc-gateway-server
call mvn clean package -DskipTests -U
cd ..

echo.
echo ============================================
echo All microservices built successfully!
echo ============================================
echo.
echo You can now:
echo 1. Start with Docker: docker-compose up -d
echo 2. Start locally: run start-local-dev.bat
echo.
pause
