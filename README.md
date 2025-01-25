# 💹 API de Gestão de Investimentos

[![Java 21](https://img.shields.io/badge/Java-21-%23ED8B00?logo=openjdk)](https://www.oracle.com/java/)  
[![Spring Boot 3.3.7](https://img.shields.io/badge/Spring%20Boot-3.3.7-%236DB33F?logo=spring)](https://spring.io/projects/spring-boot)  
[![MySQL 8.0](https://img.shields.io/badge/MySQL-8.0-%234479A1?logo=mysql)](https://www.mysql.com/)  
[![Docker](https://img.shields.io/badge/Docker-✔️-%232496ED?logo=docker)](https://www.docker.com/)

API RESTful para gerenciar portfólios de investimentos com integração em tempo real de cotações via **[Brapi](https://brapi.dev/)**.

---

## ✨ **Funcionalidades**

### 👤 **Usuários**
- ✅ Cadastro, listagem, atualização e exclusão
- 🔒 Autenticação JWT
- 🔄 Atualização parcial (username ou password)

### 💼 **Contas**
- ➕ Múltiplas contas por usuário
- 🏠 Vinculação de endereço de cobrança
- 🎯 Seleção de conta ativa

### 📈 **Investimentos**
- 🔍 Validação de tickers via Brapi
- 💰 Cálculo automático do valor total investido
- 📊 Associação de ações a contas

---

## 🛠️ **Tecnologias**

| **Categoria**        | **Tecnologias**                                                                |
|----------------------|--------------------------------------------------------------------------------|
| **Backend**          | Java 21, Spring Boot 3, Spring Data JPA, Spring Security, OpenFeign            |
| **Banco de Dados**   | MySQL 8.0 (Docker)                                                             |
| **Autenticação**     | JWT, Spring Security OAuth2                                                    |
| **APIs Externas**    | Alpha Vantage, Brapi                                                           |
| **Testes**           | JUnit 5, Mockito, Insomnia                                                     |
| **Ferramentas**      | Docker, Lombok, MapStruct                                                      |

---


## 📡 **Endpoints**

### 👤 **Autenticação**

| **Método** | **Endpoint** | **Ação**                  |
|------------|--------------|---------------------------|
| POST       | `/login`     | Gera token JWT           |

#### Exemplo de Request (POST /login):
```json
{
  "username": "admin",
  "password": "admin123"
}
```

---

### 👤 **Usuários**

| **Método** | **Endpoint** | **Ação**                  |
|------------|--------------|---------------------------|
| POST       | `/users`     | Cria usuário             |
| GET        | `/users/me`  | Retorna dados do usuário logado |
| PUT        | `/users/me`  | Atualiza username/password|
| DELETE     | `/users/me`  | Deleta a Conta            |
| POST       | `me/accounts/{accountId}/select`| Associa ao usuario logado umas das suas contas|

#### Exemplo de Request (POST /users):
```json
{
  "username": "investidor123",
  "email": "invest@example.com",
  "password": "SenhaSegura@123"
}
```

---

### 💼 **Contas**

| **Método** | **Endpoint**                  | **Ação**                    |
|------------|-------------------------------|-----------------------------|
| POST       | `/users/me/accounts`          | Cria nova conta com endereço de cobrança |
| GET        | `/users/me/accounts`          | Lista todas as contas associadas ao usuario logado |
| POST       | `/account/stock`              | Associa ação à conta ativa  |

#### Exemplo de Request (POST /users/me/accounts):
```json
{
  "description": "Conta Corrente",
  "street": "Av. Paulista",
  "number": 1000
}
```
#### Exemplo de Request(POST /account/stock)
```json
{
    "stockId" : "KO",
    "quantity" : "100"
}

```
---

### 📈 **Ações (Stocks)**

| **Método** | **Endpoint**                    | **Ação**                     |
|------------|---------------------------------|------------------------------|
| GET        | `/stock`                        | Lista todas as ações (ADMIN) |
| POST       | `/stock/search`                 | Busca ações por keyword      |

#### Exemplo de Response (GET /account/{accountId}/stock):
```json
[
  {
    "stockId": "PETR4",
    "description": "Petróleo Brasileiro S.A.",
    "quantity": 150,
    "totalValue": 7500.00
  }
]
```

---

## 🔒 **Segurança**

- **Roles**: 
  - ADMIN: Gerencia usuários e ações
  - BASIC: Operações básicas
- Endpoints protegidos com JWT no header `Authorization: Bearer <token>`.
- Senhas criptografadas com BCrypt.

