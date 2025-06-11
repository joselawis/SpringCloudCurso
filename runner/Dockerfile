# Dockerfile

# Usa una imagen base de Ubuntu
FROM ubuntu:22.04

# Instala dependencias necesarias y crea un usuario no root
RUN apt-get update && apt-get install -y \
    curl \
    docker.io \
    git \
    maven \
    openjdk-21-jdk \
    sudo \
    unzip \
    && rm -rf /var/lib/apt/lists/* \
    && useradd -m github \
    && echo "github:github" | chpasswd \
    && adduser github sudo

# Cambia al directorio del runner
WORKDIR /actions-runner

# Descarga e instala el runner
ADD https://github.com/actions/runner/releases/download/v2.316.0/actions-runner-linux-x64-2.316.0.tar.gz actions-runner.tar.gz
RUN tar xzf actions-runner.tar.gz \
    && ./bin/installdependencies.sh

# Copia el script de entrada
COPY entrypoint.sh .

# Cambia la propiedad de los archivos al usuario github
RUN chown -R github:github /actions-runner

# Cambia al usuario github
USER github

# Establece el script de entrada como el comando por defecto
ENTRYPOINT ["/actions-runner/entrypoint.sh"]
