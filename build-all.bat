@echo off
echo Building all microservices...

echo Cleaning Maven cache for this project...
call mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false -Dsilent=true

echo Building parent POM (spring-cloud-course)...
call mvn clean install -N -DskipTests -U

echo Building libs-msvc-entities (dependency)...
cd libs-msvc-entities
call mvn clean install -DskipTests -U
cd ..

echo Building libs-msvc-commons (dependency)...
cd libs-msvc-commons
call mvn clean install -DskipTests -U
cd ..

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

echo All microservices built successfully!
