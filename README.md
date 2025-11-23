# 📊 Work360 — Plataforma de Produtividade com IA

A **Work360** é uma plataforma desenvolvida para centralizar tarefas, reuniões, relatórios, métricas de produtividade e análises inteligentes com IA.  
Ela foi criada como solução para melhorar a organização de equipes, facilitar a gestão do tempo e oferecer insights automáticos através de inteligência artificial.

A aplicação utiliza **Java 21 + Spring Boot 3**, com **JWT**, **RAG**, agentes de IA e uma arquitetura modular, limpa e escalável.

---

# 🚀 Funcionalidades Principais

### ✅ **Gerenciamento de Usuários**
- Cadastro, listagem, edição e exclusão de usuários.
- Criptografia de senhas com BCrypt.
- Login seguro via JWT.

### 📝 **Tarefas**
- CRUD completo de tarefas.
- Prioridade, status e título.
- Relacionadas ao usuário.

### 🗓️ **Reuniões**
- CRUD de eventos.
- Organização de reuniões com data e descrição.

### 📈 **Analytics**
- Registro automático de eventos de produtividade.
- Extração de métricas diárias e históricas.
- Relacionado com as ações do usuário no sistema.

### 📄 **Relatórios**
- Geração de relatórios automáticos de produtividade.
- Execução de relatórios inteligentes com IA.

### 🤖 **IA Integrada**
- Agentes especializados:
  - Classificador
  - Produtividade
  - Resumo
- Orquestrador RAG para análises inteligentes sobre relatórios.

---

# 🧠 Arquitetura da Aplicação

A Work360 segue uma arquitetura limpa, modular e fácil de manter:

```
├── ai
│ ├── agents
│ │ ├── IAgenteClassificadorService
│ │ ├── IAgenteProdutividadeService
│ │ └── IAgenteResumoService
│ ├── controller
│ │ └── IAController
│ ├── orchestrator
│ │ └── IAOrquestradoraService
│ └── rag
│ ├── PromptLoader
│ ├── RAGContexto
│ ├── RAGRepository
│ └── RAGService
│
├── config
│ ├── SecurityConfiguration
│ └── SwaggerConfig
│
├── controller
│ ├── AnalyticsController
│ ├── AuthController
│ ├── RelatorioController
│ ├── ReuniaoController
│ ├── TarefaController
│ └── UsuarioController
│
├── dto
│ ├── AnalyticsEventoRequest / Response
│ ├── AnalyticsMetricaResponse
│ ├── LoginRequest / Response
│ ├── RelatorioResponse
│ ├── ReuniaoRequest / Response
│ ├── TarefaRequest / Response
│ └── UsuarioRequest / Response
│
├── handler
│ └── GlobalExceptionHandler
│
├── mapper
│ ├── AnalyticsEventoMapper
│ ├── AnalyticsMetricaMapper
│ ├── RelatorioMapper
│ ├── ReuniaoMapper
│ ├── TarefaMapper
│ └── UsuarioMapper
│
├── model
│ ├── AnalyticsEvento
│ ├── AnalyticsMetrica
│ ├── Prioridade
│ ├── Relatorio
│ ├── Reuniao
│ ├── Tarefa
│ ├── TipoEvento
│ └── Usuario
│
├── repository
│ ├── AnalyticsEventoRepository
│ ├── AnalyticsMetricaRepository
│ ├── RelatorioRepository
│ ├── ReuniaoRepository
│ ├── TarefaRepository
│ └── UsuarioRepository
│
├── security
│ ├── SecurityFilter
│ └── TokenService
│
└── service
├── AnalyticsService
├── AutenticacaoService
├── RelatorioService
├── ReuniaoService
├── TarefaService
└── UsuarioService

```

# 🔐 Autenticação (JWT)

O sistema usa:

- `AuthenticationManager`
- `SecurityFilter`
- `TokenService`
- Tokens assinados e validados em todas as rotas protegidas.

Fluxo:
1. Envia e-mail e senha para `/login`.
2. Gera token JWT.
3. O token é enviado no header `Authorization: Bearer <token>`.

---

# 📚 Endpoints (Swagger)

A API está documentada no Springdoc/Swagger e organizada em módulos:

Os principais endpoints da API Work360 incluem:

### 🔹 Módulo: Usuários

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/usuarios` | Lista todos os usuários |
| `POST` | `/usuarios` | Cria um novo usuário |
| `GET` | `/usuarios/{id}` | Retorna um usuário por ID |
| `PUT` | `/usuarios/{id}` | Atualiza um usuário existente |
| `DELETE` | `/usuarios/{id}` | Exclui um usuário por ID |

### 🔹 Módulo: Autenticação

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/login` | Realiza o login e retorna o token JWT |

### 🔹 Módulo: Tarefas

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/tarefas` | Lista todas as tarefas |
| `POST` | `/tarefas` | Cria uma nova tarefa |
| `GET` | `/tarefas/{id}` | Retorna uma tarefa por ID |
| `PUT` | `/tarefas/{id}` | Atualiza uma tarefa existente |
| `DELETE` | `/tarefas/{id}` | Exclui uma tarefa por ID |

### 🔹 Módulo: Reuniões

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/reunioes` | Lista todas as reuniões |
| `POST` | `/reunioes` | Agenda uma nova reunião |
| `GET` | `/reunioes/{id}` | Retorna uma reunião por ID |
| `PUT` | `/reunioes/{id}` | Atualiza uma reunião existente |
| `DELETE` | `/reunioes/{id}` | Cancela/Exclui uma reunião por ID |

### 🔹 Módulo: Analytics

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/analytics/eventos` | Registra um novo evento de analytics |
| `GET` | `/analytics/metricas/{usuarioId}` | Retorna todas as métricas de analytics para um usuário |
| `GET` | `/analytics/metricas/{usuarioId}/hoje` | Retorna as métricas de analytics do usuário para o dia atual |

### 🔹 Módulo: Relatórios

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `DELETE` | `/relatorios/{id}` | Exclui um relatório por ID |
| `GET` | `/relatorios/{usuarioId}` | Lista os relatórios de um usuário |
| `POST` | `/relatorios/gerar` | Inicia a geração de um novo relatório |

### 🔹 Módulo: Inteligência Artificial (IA)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/ia/relatorio/{id}` | Processa um relatório específico usando IA |
| `GET` | `/ia` | Endpoint genérico de consulta ou status da IA |


---

# 🛠️ Tecnologias Utilizadas

- Java 21  
- Spring Boot 3  
- Spring Security + JWT  
- Spring Data JPA  
- Hibernate  
- Lombok  
- SQL Server (Observado no `application.properties`)  
- RAG + Agentes de IA  
- Swagger/OpenAPI  

---

# 🏁 Como rodar o projeto

```bash
# Clone o repositório
git clone https://github.com/EduNagado/Work360.git
# Navegue até o diretório do projeto
cd Work360
# Execute o projeto (usando o wrapper do Gradle, já que o projeto usa Gradle)
./gradlew bootRun
```

A API será iniciada em:

```
http://localhost:8080
```

Swagger disponível em:

```
http://localhost:8080/swagger-ui.html
```

---

👨‍💻 Autor

* Eduardo Nagado




