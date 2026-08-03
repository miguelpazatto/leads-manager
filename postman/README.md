# Leads Manager API — QA Suite 🧪

Suíte de testes manuais e semi-automatizados construída no Postman para validar o comportamento da [Leads Manager API](../README.md), cobrindo autenticação, regras de negócio e cenários de falha em todas as entidades do domínio.

---

## 🎯 Objetivo

Validar, de fora para dentro, se a API se comporta como esperado tanto em fluxos de sucesso quanto em cenários de erro - incluindo casos que não são cobertos pelos testes unitários/integração já existentes no projeto principal (JUnit 5 + Mockito), como validação de contrato de requisição, autorização por role, e consistência de dados entre entidades relacionadas.

---

## 🗂️ Estrutura da Suíte

```
00. Autenticação
  └── [LOGIN]
      ├── Gerar Token - Login Admin (200)
      └── 01. Cenários negativos
  ├── Cadastrar Novo Usuário / Vendedor (201)
  └── Gerar Token - Login Visitante (200)

01. Smoke Tests
  ├── Criar Novo Lead - Dados Válidos (201)
  └── Listar Todos os Leads (200)

02. Salesman
  ├── 01. Cenários negativos
  ├── [SETUP] Criar Vendedor Cobaia
  ├── Alterar Vendedor - Mudança de Dados Pessoais (200)
  ├── Buscar Vendedor por ID - Alterado (200)
  ├── Listar Todos os Vendedores (200)
  └── Remover Vendedor por ID - Sucesso (204)

03. Questions
  ├── 01. Cenários Negativos
  ├── Criar Nova Questão - Dados Válidos (201)
  ├── Alterar Questão - Mudança Enunciado (200)
  ├── Listar Todas as Questões (200)
  ├── Buscar Questão por ID - Existente (200)
  └── Remover Questão por ID - Sucesso (204)

04. Options
  ├── 01. Cenários Negativos
  ├── [SETUP] Criar questão host
  ├── Cria Nova Opção - Dados Válidos (201)
  ├── Alterar Opção - Dados Válidos (200)
  ├── Listar Todas as Opções (200)
  ├── Buscar Opção por ID - Existente (200)
  ├── Remover Opção por ID - Sucesso (204)
  └── [TEARDOWN] Remover questão host

05. Leads
  ├── [SETUP]
  ├── 01. Regras de Negócio e Estados
  └── 02. Cenários negativos
      ├── Atualizar Lead - Mudança de Dados Pessoais (200)
      ├── Buscar Lead por ID - Existente (200)
      └── Remover Lead por ID - Sucesso (204)
```

Cada pasta de entidade segue o mesmo padrão: **Smoke Test** (caminho feliz mínimo) → **cenários de sucesso completos** → **cenários negativos** (regra de negócio, validação de contrato, segurança), com blocos de `[SETUP]` e `[TEARDOWN]` quando um teste depende de dado criado especificamente para ele.

---

## 🐛 Bugs encontrados durante os testes

Testar a API de fora para dentro revelou falhas que não apareciam nos testes unitários/integração do projeto principal:

| Bug | Status |
|---|---|
| `POST /auth/register` retornava **403 Forbidden** em vez de **400 Bad Request** ao enviar um Enum (Role) inválido — o erro de validação do Jackson era mascarado pelo Spring Security antes de chegar ao handler correto | ✅ Corrigido — [Issue #3](https://github.com/miguelpazatto/leads-manager/issues/3) |
| `POST /auth/register` não retornava o **ID do Salesman** recém-criado no corpo da resposta — devolvia o ID do `User` vinculado, entidade diferente | ✅ Corrigido — [Issue #2](https://github.com/miguelpazatto/leads-manager/issues/2) |
| Ausência de validação contra duplicidade de registros em determinados cadastros | Documentado e corrigido |
| Tratamento inadequado de requisição com JSON malformado | Documentado e corrigido |

Cada falha foi documentada formalmente via GitHub Issues antes da correção, seguindo o fluxo: **encontrar → reportar → corrigir → retestar**.

---

## ⚠️ Nota técnica sobre a ordem de execução

A estrutura atual desta suíte não segue rigorosamente a ordem ideal de execução — em algumas pastas, cenários negativos aparecem antes da finalização completa do caminho feliz da entidade, o que gerou dependência pontual de dados fixos inseridos via Flyway para contornar a ordem.

A estrutura tecnicamente correta separaria:
1. **Setup / Happy Path** (Create, Read, Update — sem Delete)
2. **Cenários negativos** (usando o dado criado no setup)
3. **Cleanup** (Delete como etapa final, isolada)

Mantive a estrutura atual conscientemente neste projeto de aprendizado — o objetivo aqui era validar comportamento e praticar fluxo E2E, não otimizar arquitetura de teste. A correção já será aplicada como padrão em projetos futuros.

---

## 🛠️ Ferramentas e técnicas utilizadas

- **Postman** — criação de coleções, ambientes e variáveis
- **Scripts em Pre-request e Tests** — captura dinâmica de IDs e tokens gerados em uma requisição para uso automático nas seguintes (ex.: gerar token de login e injetá-lo automaticamente no header `Authorization` das próximas chamadas)
- **Fluxos E2E encadeados** — criação → leitura → atualização → remoção da mesma entidade, validando consistência do dado ao longo do ciclo
- **Cenários negativos estruturados em três eixos:** Regras de Negócio, Validação de Contrato e Segurança/Autorização
- **Collection Runner** — execução automática de toda a suíte em sequência

---

## ⚙️ Como reproduzir os testes

**Pré-requisitos:** ambiente local do [Leads Manager API](../README.md) rodando via Docker Compose (PostgreSQL) + Spring Boot local, conforme instruções no README principal.

1. Importe a collection: [`Leads Manager API - QA Suite.postman_collection.json`](./Leads%20Manager%20API%20-%20QA%20Suite.postman_collection.json)
2. Configure um Environment no Postman com as variáveis:
   - `base_url` → `http://localhost:8080`
   - `token` → preenchido automaticamente pelo script de login
3. Rode a pasta `00. Autenticação` primeiro para gerar o token
4. Execute as demais pastas individualmente ou a suíte inteira via **Collection Runner**

---

## 🚀 Próximos passos

- Automação dos principais fluxos com **Rest Assured**, integrando os testes ao pipeline de CI/CD já existente no projeto (GitHub Actions)
- Reorganização da ordem das pastas (Setup → Sad Path → Cleanup) como padrão em futuras suítes
