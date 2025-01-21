# API de Gestão de Investimentos  

![Java](https://img.shields.io/badge/Java-21-blue)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-green)  
![MySQL](https://img.shields.io/badge/MySQL-Docker-orange)  
![JUnit](https://img.shields.io/badge/Tests-JUnit-red)  

Uma API RESTful desenvolvida em Java com Spring Boot para gerenciar usuários, contas e investimentos em ações. A aplicação permite a integração com a API Brapi para validação e consulta de ações, cálculo de valores investidos, e oferece suporte a múltiplas contas por usuário.  

---

## 🚀 **Funcionalidades**  

- **Gerenciamento de Usuários:**  
  CRUD completo para criar, listar, atualizar e deletar usuários.  
- **Gerenciamento de Contas:**  
  Cada usuário pode ter múltiplas contas vinculadas.  
- **Gerenciamento de Ações (Stocks):**  
  Cada conta pode conter várias ações com os seguintes recursos:  
  - Validação do nome da ação usando a API Brapi.  
  - Consulta do preço atual (regularMarketPrice).  
  - Cálculo do valor total investido em cada ação.  
- **Testes:**  
  - Testes de integração com **Insomnia**.  
  - Testes unitários em desenvolvimento usando **JUnit** e **Mockito**.  

---

## 🛠️ **Tecnologias Utilizadas**  

- **Java 21**  
- **Spring Boot 3.3.7**  
- **MySQL** (gerenciado com Docker)  
- **API Externa:** [Brapi](https://brapi.dev/)  
- **Ferramentas de Teste:** Insomnia, JUnit, Mockito  
# API Endpoints

## Usuários

### 1. Criar Usuário
**Endpoint:**
```
POST /users
```
**Exemplo de Corpo:**
```json
{
  "username": "username",
  "email": "@email",
  "password": "password"
}
```

### 2. Listar Todos os Usuários
**Endpoint:**
```
GET /users
```

### 3. Buscar Usuário pelo ID
**Endpoint:**
```
GET /users/{userId}
```

### 4. Atualizar Usuário
**Endpoint:**
```
PUT /users/{userId}
```
**Exemplo de Corpo:**
```json
{
  "username": "novo username"
  "password": "nova senha"
}
```
**Ele tambem pode ser Atualizado passando Apenas a senha ou username**

### 5. Deletar Usuário por ID
**Endpoint:**
```
DELETE /users/{userId}
```

---

## Contas

### 1. Criar Conta
**Endpoint:**
```
POST /users/{userId}/accounts
```
**Exemplo de Corpo:**
```json
{
  "description": "nome da conta",
  "street": "rua",
  "number": 55
}
```

### 2. Listar Contas de um Usuário
**Endpoint:**
```
GET /users/{userId}/accounts
```

---

## Stocks

### 1. Criar Stock
**Endpoint:**
```
POST /stock
```
**Exemplo de Corpo:**
```json
{
  "stockId": "TSLA"
}
```
**A Stock id passada tem que ser uma ticker valida**

### 2. Listar Todas as Stocks
**Endpoint:**
```
GET /stock
```
**Exemplo De Resposta**
```
[
	{
		"stockId": "KO",
		"description": "The Coca-Cola Company",
		"currency": "USD"
	},
	{
		"stockId": "PETR4",
		"description": "Petróleo Brasileiro S.A. - Petrobras",
		"currency": "BRL"
	}
]
```

### 3. Deletar Stock pelo Stock ID
**Endpoint:**
```
DELETE /stock/{stockId}
```

### 4. Associar Conta a uma Stock
**Endpoint:**
```
POST /account/{accountId}/stock
```
**Exemplo de Corpo:**
```json
{
  "stockId": "KO",
  "quantity": 100
}
```

### 5. Listar Todos os Stocks de uma Conta
**Endpoint:**
```
GET /account/{accountId}/stock
```
**Exemplo De Resposta**
```
  [
	{
		"stockId": "KO",
		"description": "The Coca-Cola Company",
		"quantity": 100,
		"totalValue": 6271.0
	}
]
```

