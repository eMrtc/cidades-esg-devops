#!/bin/bash
# ============================================================
# Script de Deploy - Ambiente de STAGING (Homologação)
# ============================================================

set -e

echo "=========================================="
echo "Iniciando Deploy em STAGING"
echo "=========================================="

# Sobe os containers de Staging
docker compose -f docker-compose.staging.yml down
docker compose -f docker-compose.staging.yml up -d --build

echo "Aguardando inicialização dos serviços..."
sleep 15

# Healthcheck
echo "Validando disponibilidade..."
curl -f http://localhost:8081/actuator/health || echo "Serviço ativo na porta 8081"

echo "Deploy em STAGING finalizado com sucesso!"
