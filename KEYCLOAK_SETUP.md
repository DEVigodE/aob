# AOB - Quarkus + Keycloak + H2

Projeto Quarkus com autenticação JWT via Keycloak, banco de dados H2 em memória e CRUD protegido.

## 🔧 Stack Técnica

- **Java 21**
- **Quarkus 3.30.7**
- **Keycloak** (autenticação JWT)
- **H2** (banco de dados em memória)
- **Hibernate ORM + Panache**
- **Hibernate Validator**
- **RESTEasy Reactive**

---

## ⚙️ Configuração do Keycloak

### 1. Iniciar Keycloak (Docker)

```bash
docker run -d --name keycloak \
  -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest \
  start-dev
```

Acesse: http://localhost:8080

### 2. Criar Realm `aob`

1. Login com `admin` / `admin`
2. Clique em **Create Realm**
3. Nome: `aob`
4. Clique em **Create**

### 3. Criar Client `auth-service`

1. No realm `aob`, vá em **Clients** → **Create client**
2. Configurações:
   - **Client ID**: `auth-service`
   - **Client type**: `OpenID Connect`
   - Clique em **Next**
3. Capability config:
   - **Client authentication**: `OFF` (bearer-only não precisa de secret)
   - **Authorization**: `OFF`
   - **Standard flow**: `OFF`
   - **Direct access grants**: `ON`
   - Clique em **Next** e depois **Save**

### 4. Criar Roles

1. Vá em **Realm roles** → **Create role**
2. Crie duas roles:
   - `user`
   - `admin`

### 5. Criar Usuário `admin`

1. Vá em **Users** → **Add user**
2. Configurações:
   - **Username**: `admin`
   - **Email**: `admin@aob.com` (opcional)
   - **Email verified**: `ON`
   - Clique em **Create**
3. Aba **Credentials**:
   - Clique em **Set password**
   - **Password**: `admin`
   - **Temporary**: `OFF`
   - Clique em **Save**
4. Aba **Role mapping**:
   - Clique em **Assign role**
   - Selecione `admin` e `user`
   - Clique em **Assign**

---

## 🚀 Executar o Projeto

### 1. Compilar e rodar em modo dev

```bash
./mvnw quarkus:dev
```

A aplicação estará rodando em: http://localhost:8081

### 2. Verificar endpoints públicos

```bash
# Health check
curl http://localhost:8081/public/health

# Info
curl http://localhost:8081/public/info
```

---

## 🔐 Testar Autenticação JWT

### 1. Obter Token JWT do Keycloak

```bash
curl -X POST http://localhost:8080/realms/aob/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password" \
  -d "client_id=auth-service"
```

Resposta (copie o `access_token`):
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI...",
  "token_type": "Bearer"
}
```

### 2. Usar o Token nas Requisições

Defina o token em uma variável (PowerShell):
```powershell
$TOKEN = "SEU_ACCESS_TOKEN_AQUI"
```

Ou (Bash):
```bash
export TOKEN="SEU_ACCESS_TOKEN_AQUI"
```

---

## 📦 Endpoints da API

### Públicos (sem autenticação)

#### Listar todas as partidas
```bash
curl http://localhost:8081/partidas
```

#### Buscar partida por ID
```bash
curl http://localhost:8081/partidas/1
```

### Protegidos (requer role `admin`)

#### Criar partida
```bash
curl -X POST http://localhost:8081/partidas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "timeCasa": "Flamengo",
    "timeVisitante": "Palmeiras",
    "data": "2026-02-15",
    "placarCasa": 2,
    "placarVisitante": 1
  }'
```

#### Atualizar partida
```bash
curl -X PUT http://localhost:8081/partidas/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "timeCasa": "Flamengo",
    "timeVisitante": "Palmeiras",
    "data": "2026-02-15",
    "placarCasa": 3,
    "placarVisitante": 2
  }'
```

#### Deletar partida
```bash
curl -X DELETE http://localhost:8081/partidas/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Autenticado (requer role `user` ou `admin`)

#### Ver informações do usuário autenticado
```bash
curl http://localhost:8081/partidas/me \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🧪 Testar com Postman/Insomnia

1. **Obter Token**:
   - Method: `POST`
   - URL: `http://localhost:8080/realms/aob/protocol/openid-connect/token`
   - Body (x-www-form-urlencoded):
     - `username`: `admin`
     - `password`: `admin`
     - `grant_type`: `password`
     - `client_id`: `auth-service`

2. **Usar Token**:
   - Copie o `access_token` da resposta
   - Nas requisições protegidas, adicione header:
     - `Authorization`: `Bearer SEU_TOKEN_AQUI`

---

## 📊 Banco de Dados H2

O banco H2 está configurado em memória. Os dados são perdidos ao reiniciar a aplicação.

Para acessar o console H2 (se habilitado):
- URL: `jdbc:h2:mem:partidasdb`
- User: `sa`
- Password: `sa`

---

## 🔒 Políticas de Segurança

- `/public/*` → Acesso público (sem autenticação)
- `GET /partidas` → Acesso público
- `GET /partidas/{id}` → Acesso público
- `POST /partidas` → Requer role `admin`
- `PUT /partidas/{id}` → Requer role `admin`
- `DELETE /partidas/{id}` → Requer role `admin`
- `GET /partidas/me` → Requer autenticação (roles `user` ou `admin`)

---

## 📝 Estrutura do Projeto

```
src/main/java/com/cannonana/
├── entity/
│   └── Partida.java           # Entidade JPA
├── repository/
│   └── PartidaRepository.java # Repositório Panache
├── service/
│   └── PartidaService.java    # Lógica de negócio
└── resource/
    ├── PartidaResource.java   # Endpoints CRUD protegidos
    └── PublicResource.java    # Endpoints públicos
```

---

## ✅ Validações

A entidade `Partida` possui validações:
- `timeCasa`: obrigatório (não pode ser vazio)
- `timeVisitante`: obrigatório (não pode ser vazio)
- `data`: obrigatório

Se enviar dados inválidos, receberá erro 400 com detalhes.

---

## 🐛 Troubleshooting

### Erro: "Port 8080 already in use"
O Keycloak está usando a porta 8080. A aplicação Quarkus está configurada para rodar na porta **8081**.

### Erro: "OIDC Server is not available"
Certifique-se de que o Keycloak está rodando em `http://localhost:8080` e que o realm `aob` foi criado.

### Erro: "Forbidden" ao criar partida
Verifique se:
1. O token JWT está sendo enviado no header `Authorization: Bearer TOKEN`
2. O usuário possui a role `admin` no Keycloak
3. O token não expirou (validade padrão: 5 minutos)

---

## 📚 Referências

- [Quarkus Security with OIDC](https://quarkus.io/guides/security-oidc-bearer-token-authentication)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache)
