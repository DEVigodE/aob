# Script para testar a API Quarkus + Keycloak
# Este script obtém um token JWT e testa os endpoints

Write-Host "🧪 Testando API Quarkus + Keycloak" -ForegroundColor Cyan
Write-Host ""

# 1. Testar endpoint público
Write-Host "1️⃣  Testando endpoint público..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8081/public/health" -Method Get
    Write-Host "   ✅ Endpoint público funcionando!" -ForegroundColor Green
    Write-Host "   Resposta: $response" -ForegroundColor White
} catch {
    Write-Host "   ❌ Erro ao acessar endpoint público" -ForegroundColor Red
    Write-Host "   Certifique-se de que a aplicação está rodando em http://localhost:8081" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# 2. Listar partidas (público)
Write-Host "2️⃣  Listando partidas (endpoint público)..." -ForegroundColor Yellow
try {
    $partidas = Invoke-RestMethod -Uri "http://localhost:8081/partidas" -Method Get
    Write-Host "   ✅ Partidas encontradas: $($partidas.Count)" -ForegroundColor Green
    $partidas | ForEach-Object {
        Write-Host "   - $($_.timeCasa) x $($_.timeVisitante): $($_.placarCasa) x $($_.placarVisitante)" -ForegroundColor White
    }
} catch {
    Write-Host "   ❌ Erro ao listar partidas" -ForegroundColor Red
}

Write-Host ""

# 3. Obter token JWT
Write-Host "3️⃣  Obtendo token JWT do Keycloak..." -ForegroundColor Yellow
try {
    $tokenUrl = "http://localhost:8080/realms/aob/protocol/openid-connect/token"
    $body = @{
        username = "admin"
        password = "admin"
        grant_type = "password"
        client_id = "auth-service"
    }
    
    $tokenResponse = Invoke-RestMethod -Uri $tokenUrl -Method Post -Body $body -ContentType "application/x-www-form-urlencoded"
    $token = $tokenResponse.access_token
    
    Write-Host "   ✅ Token JWT obtido com sucesso!" -ForegroundColor Green
    Write-Host "   Token (primeiros 50 caracteres): $($token.Substring(0, [Math]::Min(50, $token.Length)))..." -ForegroundColor White
} catch {
    Write-Host "   ❌ Erro ao obter token JWT" -ForegroundColor Red
    Write-Host "   Certifique-se de que:" -ForegroundColor Yellow
    Write-Host "   - Keycloak está rodando em http://localhost:8080" -ForegroundColor Yellow
    Write-Host "   - Realm 'aob' foi criado" -ForegroundColor Yellow
    Write-Host "   - Client 'auth-service' foi configurado" -ForegroundColor Yellow
    Write-Host "   - Usuário 'admin' existe com senha 'admin'" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# 4. Criar nova partida (protegido - requer role admin)
Write-Host "4️⃣  Criando nova partida (endpoint protegido)..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
    
    $novaPartida = @{
        timeCasa = "Botafogo"
        timeVisitante = "Fluminense"
        data = "2026-03-15"
        placarCasa = 2
        placarVisitante = 1
    } | ConvertTo-Json
    
    $partidaCriada = Invoke-RestMethod -Uri "http://localhost:8081/partidas" -Method Post -Headers $headers -Body $novaPartida
    Write-Host "   ✅ Partida criada com sucesso!" -ForegroundColor Green
    Write-Host "   ID: $($partidaCriada.id)" -ForegroundColor White
    Write-Host "   $($partidaCriada.timeCasa) x $($partidaCriada.timeVisitante): $($partidaCriada.placarCasa) x $($partidaCriada.placarVisitante)" -ForegroundColor White
} catch {
    Write-Host "   ❌ Erro ao criar partida" -ForegroundColor Red
    Write-Host "   Certifique-se de que o usuário 'admin' possui a role 'admin' no Keycloak" -ForegroundColor Yellow
}

Write-Host ""

# 5. Ver informações do usuário autenticado
Write-Host "5️⃣  Verificando informações do usuário autenticado..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $userInfo = Invoke-RestMethod -Uri "http://localhost:8081/partidas/me" -Method Get -Headers $headers
    Write-Host "   ✅ Usuário autenticado:" -ForegroundColor Green
    Write-Host "   $userInfo" -ForegroundColor White
} catch {
    Write-Host "   ❌ Erro ao obter informações do usuário" -ForegroundColor Red
}

Write-Host ""
Write-Host "🎉 Testes concluídos!" -ForegroundColor Green
Write-Host ""
Write-Host "💡 Para mais exemplos, veja o arquivo api-examples.http" -ForegroundColor Cyan
Write-Host ""
