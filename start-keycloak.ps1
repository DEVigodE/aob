# Script para configurar Keycloak via Docker
# Execute este script para iniciar o Keycloak automaticamente

Write-Host "🚀 Iniciando Keycloak com Docker..." -ForegroundColor Cyan

# Verificar se o container já existe
$existingContainer = docker ps -a --filter "name=keycloak" --format "{{.Names}}"

if ($existingContainer -eq "keycloak") {
    Write-Host "⚠️  Container 'keycloak' já existe. Removendo..." -ForegroundColor Yellow
    docker rm -f keycloak
}

# Iniciar novo container Keycloak
Write-Host "📦 Criando container Keycloak..." -ForegroundColor Green
docker run -d --name keycloak `
  -p 8080:8080 `
  -e KEYCLOAK_ADMIN=admin `
  -e KEYCLOAK_ADMIN_PASSWORD=admin `
  quay.io/keycloak/keycloak:latest `
  start-dev

Write-Host ""
Write-Host "✅ Keycloak iniciado com sucesso!" -ForegroundColor Green
Write-Host ""
Write-Host "📋 Informações de acesso:" -ForegroundColor Cyan
Write-Host "   URL: http://localhost:8080" -ForegroundColor White
Write-Host "   Usuário: admin" -ForegroundColor White
Write-Host "   Senha: admin" -ForegroundColor White
Write-Host ""
Write-Host "⏳ Aguardando Keycloak inicializar (pode levar 30-60 segundos)..." -ForegroundColor Yellow
Write-Host ""
Write-Host "📖 Próximos passos:" -ForegroundColor Cyan
Write-Host "   1. Acesse http://localhost:8080" -ForegroundColor White
Write-Host "   2. Faça login com admin/admin" -ForegroundColor White
Write-Host "   3. Siga as instruções em KEYCLOAK_SETUP.md para configurar:" -ForegroundColor White
Write-Host "      - Criar realm 'aob'" -ForegroundColor White
Write-Host "      - Criar client 'auth-service'" -ForegroundColor White
Write-Host "      - Criar roles 'user' e 'admin'" -ForegroundColor White
Write-Host "      - Criar usuário 'admin' com role 'admin'" -ForegroundColor White
Write-Host ""
Write-Host "💡 Para ver os logs do Keycloak:" -ForegroundColor Cyan
Write-Host "   docker logs -f keycloak" -ForegroundColor White
Write-Host ""
Write-Host "🛑 Para parar o Keycloak:" -ForegroundColor Cyan
Write-Host "   docker stop keycloak" -ForegroundColor White
Write-Host ""
