# Plataforma DevOps com Microsserviços, Observabilidade e Pipeline CI/CD

Projeto Final de Semestre da disciplina de Engenharia de Software do INSTITUTO FEDERAL Ceará, Campus Fortaleza.

**Equipe:**
* Matheus de Sousa Almeida
* Vinicius Silva Pereira

---

## 🏛️ Arquitetura e Modelo Conceitual

A arquitetura da plataforma foi desenhada separando o modelo conceitual em três contextos de microsserviços isolados. Para garantir o baixo acoplamento, substituímos as chaves estrangeiras (FOREIGN KEY) físicas por referências lógicas (Soft FKs) sempre que um relacionamento cruza a fronteira de um serviço.

### 1. Serviço de Autenticação (`auth-service`)
* **Stack:** Java 21, Spring Boot 3
* **Persistência:** MySQL
* **Responsabilidade:** Atua como o provedor de identidade da plataforma. Faz o gerenciamento centralizado de identidades, isola credenciais de acesso e emite tokens JWT de forma segura.

### 2. Serviço Acadêmico (`academic-service`)
* **Stack:** Python, FastAPI, SQLAlchemy
* **Persistência:** MySQL
* **Responsabilidade:** Gerenciamento do núcleo acadêmico fortemente relacional. Controla as entidades estruturadas: Alunos, Professores, Disciplinas, Turmas e Matrículas.

### 3. Serviço de Tarefas (`assignment-service`)
* **Stack:** Java 21, Spring Boot 3
* **Persistência:** MySQL
* **Responsabilidade:** Gerenciamento de atividades acadêmicas e recepção de entregas (submissões). O uso de NoSQL viabiliza a flexibilidade de schema para descrições dinâmicas e suporta alta carga de escritas.

---

## 🔄 Fluxos de Comunicação

O tráfego de dados e o isolamento da stack são resolvidos através de dois fluxos principais:

* **Fluxo A (Assíncrono via JWT):** O cliente autentica no `auth-service` e recebe um Token JWT assinado. O `academic-service` e o `assignment-service` não sobrecarregam a rede, pois validam a assinatura do token localmente.
* **Fluxo B (Síncrono via REST):** Quando os serviços precisam se falar diretamente. Por exemplo, antes do `assignment-service` salvar uma entrega no MongoDB, ele faz um `GET /api/alunos/{id}` no `academic-service` (usando a rede interna do Docker) para garantir que o aluno de fato existe. 

---

## 🚀 Como executar localmente (Infraestrutura Base)

Para testes iniciais e para subir a camada de persistência poligota, utilize o Docker Compose:

```bash
docker compose up -d mysql-auth mysql-academic mongo-db
