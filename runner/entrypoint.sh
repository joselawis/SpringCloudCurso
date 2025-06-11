#!/bin/bash
set -e

# Directorio del runner
RUNNER_DIR=/actions-runner
cd "$RUNNER_DIR"

# Si no existe el archivo de configuración, registramos el runner
if [ ! -f .credentials ]; then
  echo "➡️ Configurando el runner por primera vez..."
  ./config.sh --url "$RUNNER_REPO" \
              --token "$RUNNER_TOKEN" \
              --name "$RUNNER_NAME" \
              --work "$RUNNER_WORKDIR" \
              --unattended \
              --replace
else
  echo "✅ Runner ya configurado, saltando registro."
fi

# Capturamos SIGINT/SIGTERM para limpieza limpia
cleanup() {
  echo "🗑️ Eliminando runner de GitHub..."
  ./config.sh remove --unattended --token "$RUNNER_TOKEN"
  exit 0
}
trap 'cleanup' INT TERM

# Finalmente, ejecutamos el daemon del runner
echo "🚀 Iniciando el runner..."
exec ./run.sh
