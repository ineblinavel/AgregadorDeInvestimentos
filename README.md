# 💹 Agregador de Investimentos API

[![Java 21](https://img.shields.io/badge/Java-21-%23ED8B00?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot 3.3.7](https://img.shields.io/badge/Spring%20Boot-3.3.7-%236DB33F?logo=spring)](https://spring.io/projects/spring-boot)
[![MySQL 8.0](https://img.shields.io/badge/MySQL-8.0-%234479A1?logo=mysql)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-✔️-%232496ED?logo=docker)](https://www.docker.com/)

API RESTful para gerenciamento de portfólios de investimentos com integração em tempo real de cotações via [Brapi](https://brapi.dev/) e [Alpha Vantage](https://www.alphavantage.co/).

## 📋 Sumário
- [Funcionalidades](#-funcionalidades)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Tecnologias](#-tecnologias)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Segurança](#-segurança)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

## ✨ Funcionalidades

### 👤 Gestão de Usuários
- Cadastro e autenticação com JWT
- Gerenciamento de perfil
- Múltiplas contas por usuário
- Níveis de acesso (ADMIN/BASIC)

### 💼 Gestão de Contas
- Criação de múltiplas contas
- Endereço de cobrança vinculado
- Seleção de conta ativa
- Histórico de transações

### 📈 Gestão de Investimentos
- Integração com cotações em tempo real
- Validação automática de tickers
- Cálculo de valor total investido
- Acompanhamento de carteira

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/luanr/agregadorinvestimentos/
│   │       ├── client/          # Integrações externas
│   │       ├── config/          # Configurações Spring
│   │       ├── controller/      # Endpoints da API
│   │       ├── dto/            # Objetos de transferência
│   │       ├── entity/         # Entidades JPA
│   │       ├── exception/      # Tratamento de exceções
│   │       ├── mapper/         # Conversores DTO-Entity
│   │       ├── repository/     # Interfaces de persistência
│   │       └── service/        # Lógica de negócio
│   └── resources/
│       └── application.properties
```

## 🛠️ Tecnologias

| Categoria | Tecnologias |
|-----------|-------------|
| Backend | Java 21, Spring Boot 3.4.2 |
| Segurança | Spring Security, JWT |
| Banco de Dados | MySQL 8.0 |
| Cache & Resiliência | Spring Cache, Resilience4j |
| Documentação | OpenAPI (Swagger) |
| Containerização | Docker |
| APIs Externas | Brapi, Alpha Vantage |

## 🚀 Instalação

### Pré-requisitos
- Java 21
- Docker e Docker Compose
- Maven
- Git

### Clonando o Repositório
```bash
git clone https://github.com/seu-usuario/agregador-investimentos.git
cd agregador-investimentos
```

## ⚙️ Configuração

1. **Configuração do Ambiente**
   ```bash
   # Copie o arquivo de exemplo de variáveis de ambiente
   cp .env.example .env
   ```

2. **Gere as Chaves JWT**
   ```bash
   # Na pasta src/main/resources
   openssl genrsa > app.key
   openssl rsa -in app.key -pubout -out app.pub
   ```

3. **Configure as Variáveis de Ambiente**
   ```env
   # Banco de Dados
   MYSQL_DATABASE=nome_banco
   MYSQL_USER=usuario
   MYSQL_PASSWORD=senha
   MYSQL_ROOT_PASSWORD=senha_root
   MYSQL_HOST=localhost
   MYSQL_PORT=3306

   # APIs
   BRAPI_TOKEN=seu_token
   ALPHA_VANTAGE_API=seu_token

   # JWT
   JWT_PRIVATE_KEY=caminho/app.key
   JWT_PUBLIC_KEY=caminho/app.pub

   # Admin
   ADMIN_USERNAME=admin
   ADMIN_EMAIL=admin@email.com
   ADMIN_PASSWORD=senha_admin
   ```

## 🏃 Executando o Projeto

1. **Inicie o Banco de Dados**
   ```bash
   docker-compose up -d
   ```

2. **Execute a Aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Acesse a Documentação**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/v3/api-docs

## 📡 API Endpoints

### Autenticação

```http
POST /login
Content-Type: application/json

{
    "username": "seu_usuario",
    "password": "sua_senha"
}
```

### Usuários

```http
# Criar usuário
POST /users
Content-Type: application/json

{
    "username": "novo_usuario",
    "email": "usuario@email.com",
    "password": "senha123"
}

# Obter perfil
GET /users/me
Authorization: Bearer {token}
```

### Contas

```http
# Criar conta
POST /users/me/accounts
Authorization: Bearer {token}
Content-Type: application/json

{
    "description": "Conta Principal",
    "street": "Av Paulista",
    "number": 1000
}

# Listar contas
GET /users/me/accounts
Authorization: Bearer {token}
```

### Investimentos

```http
# Buscar ações
POST /stock/search
Content-Type: application/json

{
    "keyword": "PETR"
}

# Associar ação à conta
POST /account/stock
Authorization: Bearer {token}
Content-Type: application/json

{
    "stockId": "PETR4",
    "quantity": 100
}
```

## 🔒 Segurança

### Autenticação
- JWT Bearer Token
- Tokens com expiração de 450 segundos
- Refresh token não implementado

### Autorização
- ADMIN: Acesso total ao sistema
- BASIC: Operações básicas de usuário

### Criptografia
- Senhas com BCrypt
- Chaves RSA para JWT


## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Suporte

- Documentação: /swagger-ui.html
- API Docs: /v3/api-docs
- GitHub Issues
- Email: 

