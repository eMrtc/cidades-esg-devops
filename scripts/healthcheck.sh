#!/bin/bash
# ============================================================
# Script de Validação de Healthcheck e Endpoints
# ============================================================

PORT=${1:-8080}
HOST="http://localhost:$PORT"

echo "🔍 Testando conectividade em $HOST ..."

# Teste na raiz / API pública
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$HOST/api/orientacoes" || echo "000")

if [ "$HTTP_CODE" -eq 200 ] || [ "$HTTP_CODE" -eq 401 ] || [ "$HTTP_CODE" -eq 403 ]; then
  echo "✅ Aplicação respondendo perfeitamente (HTTP $HTTP_CODE)."
else
  echo "⚠️ Aplicação retornou código $HTTP_CODE ou está iniciando."
fi
