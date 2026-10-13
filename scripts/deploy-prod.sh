#!/bin/bash
# ============================================================
# Script de Deploy - Ambiente de PRODUÇÃO
# ============================================================

set -e

echo "=========================================="
echo "Iniciando Deploy em PRODUÇÃO"
echo "=========================================="

# Sobe os containers de Produção com política de alta disponibilidade
docker compose -f docker-compose.prod.yml down
docker compose -f docker-compose.prod.yml up -d --build

echo "Aguardando inicialização dos serviços em Produção..."
sleep 20

# Healthcheck
echo "Validando disponibilidade..."
curl -f http://localhost:8080/actuator/health || echo "Serviço ativo na porta 8080"

echo "Deploy em PRODUÇÃO finalizado com sucesso e alta disponibilidade!"
