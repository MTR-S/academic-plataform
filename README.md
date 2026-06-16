# [cite_start]Plataforma DevOps com Microsserviços, Observabilidade e Pipeline CI/CD [cite: 5]

[cite_start]Projeto Final de Semestre da disciplina de Engenharia de Software [cite: 4] [cite_start]do INSTITUTO FEDERAL Ceará, Campus Fortaleza[cite: 1, 2, 3].

**Equipe:**
* [cite_start]Matheus de Sousa Almeida [cite: 7]
* [cite_start]Vinicius Silva Pereira [cite: 8]

---

## [cite_start]🏛️ Arquitetura e Modelo Conceitual [cite: 10]

[cite_start]A arquitetura da plataforma foi desenhada separando o modelo conceitual em três contextos de microsserviços isolados[cite: 13]. [cite_start]Para garantir o baixo acoplamento, substituímos as chaves estrangeiras (FOREIGN KEY) físicas por referências lógicas (Soft FKs) sempre que um relacionamento cruza a fronteira de um serviço[cite: 14].

### [cite_start]1. Serviço de Autenticação (`auth-service`) [cite: 15]
* [cite_start]**Stack:** Java 21, Spring Boot 3 [cite: 61]
* [cite_start]**Persistência:** MySQL [cite: 61]
* **Responsabilidade:** Atua como o provedor de identidade da plataforma. [cite_start]Faz o gerenciamento centralizado de identidades, isola credenciais de acesso e emite tokens JWT de forma segura[cite: 62, 63].

### [cite_start]2. Serviço Acadêmico (`academic-service`) [cite: 17]
* [cite_start]**Stack:** Python, FastAPI, SQLAlchemy [cite: 66]
* [cite_start]**Persistência:** MySQL [cite: 67]
* **Responsabilidade:** Gerenciamento do núcleo acadêmico fortemente relacional. [cite_start]Controla as entidades estruturadas: Alunos, Professores, Disciplinas, Turmas e Matrículas[cite: 68].

### [cite_start]3. Serviço de Tarefas (`assignment-service`) [cite: 23]
* [cite_start]**Stack:** Java 21, Spring Boot 3 [cite: 72]
* [cite_start]**Persistência:** MongoDB [cite: 72]
* [cite_start]**Responsabilidade:** Gerenciamento de atividades acadêmicas e recepção de entregas (submissões)[cite: 73]. [cite_start]O uso de NoSQL viabiliza a flexibilidade de schema para descrições dinâmicas e suporta alta carga de escritas[cite: 74].

---

## [cite_start]🔄 Fluxos de Comunicação [cite: 26]

[cite_start]O tráfego de dados e o isolamento da stack são resolvidos através de dois fluxos principais[cite: 28]:

* [cite_start]**Fluxo A (Assíncrono via JWT):** O cliente autentica no `auth-service` e recebe um Token JWT assinado[cite: 30, 31]. [cite_start]O `academic-service` e o `assignment-service` não sobrecarregam a rede, pois validam a assinatura do token localmente[cite: 33].
* **Fluxo B (Síncrono via REST):** Quando os serviços precisam se falar diretamente. [cite_start]Por exemplo, antes do `assignment-service` salvar uma entrega no MongoDB, ele faz um `GET /api/alunos/{id}` no `academic-service` (usando a rede interna do Docker) para garantir que o aluno de fato existe[cite: 34, 36, 37]. 

---

## 🚀 Como executar localmente (Infraestrutura Base)

[cite_start]Para testes iniciais e para subir a camada de persistência poligota, utilize o Docker Compose[cite: 76]:

```bash
docker compose up -d mysql-auth mysql-academic mongo-db
