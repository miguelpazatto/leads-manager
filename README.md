# Leads Manager API 🚀
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-(Neon)-blue)
![Docker](https://img.shields.io/badge/Container-Docker-2496ED)
![Render](https://img.shields.io/badge/Deploy-Render-black)

Uma API RESTful desenvolvida para otimizar a produtividade de times de vendas. O sistema transforma formulários web em canais de qualificação inteligentes, classificando leads automaticamente com base em um motor de pontuação dinâmico.

---

## 🎯 O Problema que Resolve

Times de vendas perdem tempo analisando contatos frios. O Leads Manager recebe os dados do formulário, aplica uma lógica de scoring instantânea e entrega ao vendedor uma lista priorizada com sugestões de abordagem personalizadas.

---

## 🧠 Motor de Classificação (Lead Scoring)

O grande diferencial da API é seu motor de qualificação dinâmico:

1. **Pesos e Respostas (`Answers`):** Cada opção do formulário tem um peso (`weight`). Quando um lead é submetido, o sistema gera uma lista de `Answers` que registra e "congela" o peso de cada opção no momento da criação.
2. **Cálculo (`totalScore`):** Um método interno usa Streams/Lambdas para somar todos os pesos e calcular o score total do lead.
3. **Classificação Enum-Driven:** O `totalScore` é repassado ao Enum `LeadClassification`, que avalia a faixa de pontos e retorna a classificação correta.
4. **Lógica de Temperatura:** Quanto menor a pontuação (indicando maior dor do cliente), mais "Quente" (HOT) é o lead.
5. **Mensageria Encapsulada:** O Enum encapsula mensagens customizadas — uma para o usuário final e outra com sugestão de abordagem para o vendedor.

---

## 🏗️ Modelagem do Domínio

```text
Question (1) ──── (N) Option
                        │
                        └──── Answer (entidade associativa)
                                    │
Lead (1) ──────────────────── (N) Answer
  │
  └── LeadStatus (enum): NEW | CONTACTED
  └── LeadClassification (enum): HOT | WARM | COLD

Salesman (1) ──── (N) Lead
User (1) ──────── (1) Salesman
```

---

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Segurança | Spring Security + JWT |
| Banco de Dados | PostgreSQL (Hospedado no Neon) |
| Migrações | Flyway |
| Validação | Bean Validation (Jakarta) |
| Testes | JUnit 5 + Mockito |
| Documentação | SpringDoc OpenAPI (Swagger UI) |
| Tratamento de Erros | ControllerAdvice (JSONs padronizados 400/409) |
| Deploy | Render (Conteinerizado com Docker) |

---

## 🌐 Demonstração ao Vivo

**URL Base:** `https://leads-manager-smik.onrender.com`

**Swagger UI:** `https://leads-manager-smik.onrender.com/swagger-ui/index.html`

> **⚠️ Nota sobre o ambiente de demonstração:**
> Esta API está hospedada em um serviço de nuvem gratuito (Render). Caso o servidor esteja inativo há algum tempo, **a primeira requisição pode levar cerca de 50 segundos** para "acordar" o contêiner Docker. As requisições seguintes responderão instantaneamente.

**Credenciais de visitante (acesso de leitura):**
```text
Login: visitant
Senha: demo123
```

---

## 🔐 Autenticação

A API usa autenticação stateless via JWT. Para acessar rotas protegidas:

**1. Gerar token:**
```http
POST /auth/login
Content-Type: application/json

{
  "login": "visitant",
  "password": "demo123"
}
```

**2. Usar o token nas requisições:**
```text
Authorization: Bearer <token>
```

**No Swagger UI:** clique em **Authorize** (cadeado), cole o token no formato `Bearer <token>` e confirme.

---

## 📍 Mapeamento de Rotas

### Públicas (sem autenticação)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/login` | Gera token JWT |
| POST | `/leads` | Cria novo lead via formulário |
| GET | `/leads/public/{id}` | Retorna feedback amigável para o usuário final |

### Autenticadas (requerem token JWT)
| Método | Rota | Role | Descrição |
|--------|---|---|---|
| GET    | `/leads` | ADMIN, COLLABORATOR | Lista todos os leads |
| GET    | `/leads/{id}` | ADMIN, COLLABORATOR | Detalha um lead específico |
| PUT    | `/leads/{id}/contacted` | ADMIN, COLLABORATOR | Altera status para CONTACTED |
| POST   | `/salesman` | ADMIN | Cadastra novo vendedor |
| PUT    | `/salesman/{id}` | ADMIN | Atualiza vendedor |
| DELETE | `/salesman/{id}` | ADMIN | Remove vendedor |
| POST   | `/questions` | ADMIN | Cadastra nova questão |
| POST   | `/options` | ADMIN | Cadastra nova opção |
| POST   | `/auth/register` | ADMIN | Registra novo usuário |

---

## 📦 Exemplos de Payload

### Criar Lead (POST /leads)
```json
{
  "name": "Startup Inovadora Ltda",
  "email": "contato@startup.com",
  "phone": "16999999999",
  "optionsId": [1, 4, 7]
}
```

**Regras de validação:**
- `name`: obrigatório
- `email`: formato válido
- `phone`: apenas números, mínimo 11 caracteres
- `optionsId`: lista não pode ser vazia

### Login (POST /auth/login)
```json
{
  "login": "visitant",
  "password": "demo123"
}
```

---

## ⚙️ Como Rodar Localmente

### Pré-requisitos
- Java 21
- PostgreSQL
- Maven

### Configuração

1. Clone o repositório:
```bash
git clone [https://github.com/miguelpazatto/leads-manager](https://github.com/miguelpazatto/leads-manager)
cd leads-manager
```

2. Crie o banco de dados:
```sql
CREATE DATABASE leadsmanager_db;
```

3. Configure as variáveis de ambiente ou edite `application-dev.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/leadsmanager_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
api.security.token.secret=sua_chave_secreta
```

4. Rode o projeto:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

O Flyway criará as tabelas e inserirá os dados iniciais automaticamente.

5. Acesse o Swagger UI:
```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🧪 Testes

O projeto conta com cobertura de testes unitários e de integração em todas as camadas:

```bash
./mvnw test
```

- **Testes unitários:** Services com Mockito (isolamento de dependências)
- **Testes de integração:** Controllers com MockMvc + banco H2 em memória

---

## 🏛️ Arquitetura

```text
src/
├── entities/          # Entidades JPA
│   └── enums/         # Enums de domínio (LeadStatus, LeadClassification, UserRole)
├── dto/               # Data Transfer Objects (request e response)
├── repositories/      # Interfaces JPA Repository
├── services/          # Regras de negócio
├── resources/         # Controllers REST
├── infra/
│   └── security/      # Configuração Spring Security, JWT Filter, Token Service
└── resources/
    └── db/migration/  # Scripts Flyway (V1, V2)
```