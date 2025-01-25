# Melhorias para Implementação (Backlog Técnico)

Este documento lista melhorias complexas para o sistema, organizadas como *issues* no estilo GitHub.

---

## 📌 **1. Resiliência em Chamadas Externas**
**Labels**: `priority-high` `backend` `feign`  
**Descrição**:  
Adicionar circuit breakers, retries e fallbacks para APIs Brapi e Alpha Vantage.  
**Complexidade**: ⚠️⚠️⚠️ (Alta)  
**Tarefas**:
- [ ] Integrar Resilience4j para retentativas e circuit breakers.
- [ ] Implementar fallbacks (ex: retornar dados em cache se API falhar).
- [ ] Testar cenários de falha (ex: timeout, 5xx errors).

---

## 📌 **2. Documentação da API com Swagger**
**Labels**: `documentation` `priority-medium`  
**Descrição**:  
Gerar documentação automatizada dos endpoints.  
**Complexidade**: ⚠️⚠️ (Média)  
**Tarefas**:
- [ ] Adicionar dependência do SpringDoc (`springdoc-openapi`).
- [ ] Anotar controllers com `@Operation` e `@ApiResponse`.
- [ ] Publicar doc em `/swagger-ui.html`.

---

## 📌 **3. Cache para Reduzir Chamadas Externas**
**Labels**: `performance` `cache` `priority-high`  
**Descrição**:  
Cachear preços de ações e resultados de busca para otimizar performance.  
**Complexidade**: ⚠️⚠️⚠️ (Alta)  
**Tarefas**:
- [ ] Configurar Redis ou Caffeine como provedor de cache.
- [ ] Adicionar `@Cacheable` em métodos críticos (ex: `getTotal`).
- [ ] Definir TTL (ex: 10 minutos para preços de ações).

---

## 📌 **4. Refatorar FetchType para LAZY**
**Labels**: `jpa` `performance` `priority-medium`  
**Descrição**:  
Evitar `LazyInitializationException` e otimizar queries.  
**Complexidade**: ⚠️⚠️⚠️ (Alta)  
**Tarefas**:
- [ ] Alterar `FetchType.EAGER` para `LAZY` em entidades (ex: `User.accounts`).
- [ ] Usar `JOIN FETCH` em repositórios onde necessário.

---

## 📌 **5. Tratamento Global de Exceções**
**Labels**: `error-handling` `priority-low`  
**Descrição**:  
Centralizar respostas de erro em um `@ControllerAdvice`.  
**Complexidade**: ⚠️ (Baixa)  
**Tarefas**:
- [ ] Criar classe `GlobalExceptionHandler`.
- [ ] Mapear exceções para JSON padronizado.

---

## 📌 **6. Logs Estratégicos**
**Labels**: `logging` `monitoring`  
**Descrição**:  
Rastrear operações críticas e integrar com ELK/Grafana.  
**Complexidade**: ⚠️⚠️ (Média)  
**Tarefas**:
- [ ] Adicionar `log.info()` em serviços (ex: criação de usuários).
- [ ] Configurar padrão de logs em JSON.

---

## 📌 **7. Testes de Integração**
**Labels**: `testing` `priority-high`  
**Descrição**:  
Garantir estabilidade com testes end-to-end.  
**Complexidade**: ⚠️⚠️⚠️ (Alta)  
**Tarefas**:
- [ ] Usar Testcontainers para mockar banco de dados.
- [ ] Mockar APIs externas com WireMock.

---

## 📌 **8. Gerenciamento Seguro de Credenciais**
**Labels**: `security` `vault`  
**Descrição**:  
Remover credenciais hard-coded e usar Vault.  
**Complexidade**: ⚠️⚠️ (Média)  
**Tarefas**:
- [ ] Migrar `ADMIN_PASSWORD` para variáveis de ambiente.
- [ ] Integrar com Spring Cloud Vault.

---

## 🚀 **Priorização Sugerida**
1. **Resiliência em APIs** → Evitar falhas em produção.
2. **Testes de Integração** → Garantir qualidade.
3. **Cache** → Reduzir custos operacionais.

---

