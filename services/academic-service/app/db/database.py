from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base
from app.core.config import settings

# 1. Cria o Motor de Conexão usando a URL que vem do nosso config.py
# O echo=True imprime no terminal todos os comandos SQL gerados (ótimo para debug)
engine = create_engine(settings.DATABASE_URL, echo=True)

# 2. Cria a Fábrica de Sessões
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# 3. Classe Base para nossos futuros modelos (Aluno, Turma, etc.)
Base = declarative_base()

# 4. Injeção de dependência para as rotas do FastAPI
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()