# 🚀 Quick Start - Projeto Quarkus + Keycloak

## ✅ O que foi criado

Projeto Quarkus completo com:
- ✅ Autenticação JWT via Keycloak
- ✅ Banco de dados H2 em memória
- ✅ CRUD de Partidas com proteção por roles
- ✅ Validação de dados com Hibernate Validator
- ✅ Dados de exemplo pré-carregados
- ✅ Scripts de automação

## 📁 Arquivos Criados

### Código Java
- `entity/Partida.java` - Entidade JPA com validações
- `repository/PartidaRepository.java` - Repositório Panache
- `service/PartidaService.java` - Lógica de negócio
- `resource/PartidaResource.java` - Endpoints CRUD protegidos
- `resource/PublicResource.java` - Endpoints públicos
- `DataInitializer.java` - Popula banco com dados de exemplo

### Configuração
- `pom.xml` - Atualizado com todas as dependências
- `application.properties` - Configuração H2 + Keycloak OIDC

### Documentação e Scripts
- `KEYCLOAK_SETUP.md` - Guia completo de configuração
- `api-examples.http` - Exemplos de requisições HTTP
- `start-keycloak.ps1` - Script para iniciar Keycloak
- `test-api.ps1` - Script para testar a API
- `QUICK_START.md` - Este arquivo

## 🏃 Como Executar (3 passos)

### 1️⃣ Iniciar Keycloak

```powershell
.\start-keycloak.ps1
```

Aguarde 30-60 segundos e acesse http://localhost:8080
- Login: `admin` / `admin`
- Siga as instruções em `KEYCLOAK_SETUP.md` para configurar:
  - Criar realm `aob`
  - Criar client `auth-service`
  - Criar roles `user` e `admin`
  - Criar usuário `admin` com role `admin`

### 2️⃣ Iniciar a Aplicação Quarkus

```powershell
.\mvnw quarkus:dev
```

A aplicação estará em: http://localhost:8081

### 3️⃣ Testar a API

Opção A - Script automatizado:
```powershell
.\test-api.ps1
```

Opção B - Manualmente com curl/Postman:
Veja exemplos em `api-examples.http`

## 🔐 Endpoints

### Públicos (sem autenticação)
- `GET /public/health` - Health check
- `GET /public/info` - Informações da API
- `GET /partidas` - Listar todas as partidas
- `GET /partidas/{id}` - Buscar partida por ID

### Protegidos (requer role `admin`)
- `POST /partidas` - Criar partida
- `PUT /partidas/{id}` - Atualizar partida
- `DELETE /partidas/{id}` - Deletar partida

### Autenticados (requer role `user` ou `admin`)
- `GET /partidas/me` - Ver informações do usuário

## 🧪 Teste Rápido

1. **Endpoint público:**
```bash
curl http://localhost:8081/partidas
```

2. **Obter token:**
```bash
curl -X POST http://localhost:8080/realms/aob/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin&password=admin&grant_type=password&client_id=auth-service"
```

3. **Criar partida (use o token obtido):**
```bash
curl -X POST http://localhost:8081/partidas \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "timeCasa": "Flamengo",
    "timeVisitante": "Vasco",
    "data": "2026-03-10",
    "placarCasa": 3,
    "placarVisitante": 0
  }'
```

## 📚 Documentação Completa

Para mais detalhes, consulte:
- `KEYCLOAK_SETUP.md` - Configuração detalhada do Keycloak
- `api-examples.http` - Todos os exemplos de requisições

## 🐛 Problemas Comuns

**Erro: "OIDC Server is not available"**
- Certifique-se de que o Keycloak está rodando
- Verifique se o realm `aob` foi criado

**Erro: "Forbidden" ao criar partida**
- Verifique se o usuário tem a role `admin`
- Verifique se o token não expirou (5 minutos)

**Erro: "Port 8080 already in use"**
- A aplicação Quarkus roda na porta 8081
- O Keycloak roda na porta 8080

## 🎯 Próximos Passos

- [ ] Adicionar mais entidades (Jogadores, Times, etc.)
- [ ] Implementar paginação nas listagens
- [ ] Adicionar filtros de busca
- [ ] Criar testes unitários e de integração
- [ ] Configurar perfis (dev, prod)
- [ ] Adicionar Swagger/OpenAPI

## 📞 Suporte

Para dúvidas ou problemas, consulte:
- [Documentação Quarkus](https://quarkus.io/guides/)
- [Documentação Keycloak](https://www.keycloak.org/documentation)
