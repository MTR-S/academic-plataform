from contextlib import asynccontextmanager
from fastapi import FastAPI
import logging
import sys

from app.db.database import engine, Base
from app.models import academic
from app.api.endpoints import health

# IMPORTANTE: Importamos as rotas do Aluno que acabamos de criar
from app.api.endpoints import aluno, disciplina, professor, turma, matricula

# --- CONFIGURAÇÃO PADRÃO DE LOGS (Estilo Spring Boot) ---
logging.basicConfig(
    stream=sys.stdout,
    level=logging.INFO,
    format="%(asctime)s - [%(levelname)s] - %(name)s : %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S"
)
logger = logging.getLogger(__name__)
# --------------------------------------------------------

@asynccontextmanager
async def lifespan(app: FastAPI):
    logger.info("Verificando e criando tabelas no MySQL...")
    Base.metadata.create_all(bind=engine)
    logger.info("Tabelas verificadas/criadas com sucesso!")
    yield  
    logger.info("Encerrando o Academic Service...")

app = FastAPI(
    title="Academic Service API",
    description="Núcleo transacional acadêmico da plataforma DevOps do IFCE",
    version="1.0.0",
    lifespan=lifespan
)

# IMPORTANTE: Plugamos as rotas no aplicativo principal
# Tudo que for da rota de alunos ficará no endereço /api/alunos
app.include_router(aluno.router, prefix="/api/alunos", tags=["Alunos"])
app.include_router(disciplina.router, prefix="/api/disciplinas", tags=["Disciplinas"])
app.include_router(professor.router, prefix="/api/professores", tags=["Professores"])
app.include_router(turma.router, prefix="/api/turmas", tags=["Turmas"])
app.include_router(matricula.router, prefix="/api/matriculas", tags=["Matrículas"])

# Adicionamos a nova rota de health robusta (sem prefixo, para ficar na raiz /health)
app.include_router(health.router)