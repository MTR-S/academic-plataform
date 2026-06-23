from sqlalchemy import Column, Integer, String, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from datetime import datetime
from app.db.database import Base

# 1. Entidade Professor
class Professor(Base):
    __tablename__ = "professores"

    id = Column(Integer, primary_key=True, index=True)
    siape = Column(String(20), unique=True, nullable=False)
    departamento = Column(String(50), nullable=False)
    
    # Referência Lógica (Soft FK) para o auth-service do Matheus
    usuario_id = Column(Integer, unique=True, nullable=False) 

    # Um professor leciona várias turmas (1:N)
    turmas = relationship("Turma", back_populates="professor")

# 2. Entidade Aluno
class Aluno(Base):
    __tablename__ = "alunos"

    id = Column(Integer, primary_key=True, index=True)
    matricula = Column(String(20), unique=True, nullable=False)
    curso = Column(String(100), nullable=False)
    
    # Referência Lógica (Soft FK) para o auth-service
    usuario_id = Column(Integer, unique=True, nullable=False) 

    # Um aluno tem várias matrículas
    matriculas_rel = relationship("Matricula", back_populates="aluno")

# 3. Entidade Disciplina
class Disciplina(Base):
    __tablename__ = "disciplinas"

    id = Column(Integer, primary_key=True, index=True)
    nome = Column(String(100), nullable=False)
    codigo = Column(String(20), unique=True, nullable=False)
    carga_horaria = Column(Integer, nullable=False)

    # Uma disciplina baseia várias turmas
    turmas = relationship("Turma", back_populates="disciplina")

# 4. Entidade Turma
class Turma(Base):
    __tablename__ = "turmas"

    id = Column(Integer, primary_key=True, index=True)
    semestre = Column(String(10), nullable=False)
    codigo = Column(String(20), unique=True, nullable=False)    
    # Chaves Estrangeiras Físicas (dentro do mesmo microsserviço)
    disciplina_id = Column(Integer, ForeignKey("disciplinas.id"), nullable=False)
    professor_id = Column(Integer, ForeignKey("professores.id"), nullable=False)

    # Mapeamento Bidirecional
    disciplina = relationship("Disciplina", back_populates="turmas")
    professor = relationship("Professor", back_populates="turmas")
    matriculas_rel = relationship("Matricula", back_populates="turma")

# 5. Entidade Matrícula (Agora como Entidade Completa)
class Matricula(Base):
    __tablename__ = "matriculas"

    id = Column(Integer, primary_key=True, index=True)
    data = Column(DateTime, default=datetime.utcnow)
    status = Column(String(20), default="ATIVA", nullable=False)
    
    # Chaves Estrangeiras Físicas
    aluno_id = Column(Integer, ForeignKey("alunos.id"), nullable=False)
    turma_id = Column(Integer, ForeignKey("turmas.id"), nullable=False)

    # Mapeamento Bidirecional
    aluno = relationship("Aluno", back_populates="matriculas_rel")
    turma = relationship("Turma", back_populates="matriculas_rel")