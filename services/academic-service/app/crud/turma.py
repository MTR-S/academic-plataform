from sqlalchemy.orm import Session
from app.models import academic
from app.schemas import turma

def get_turma(db: Session, turma_id: int):
    return db.query(academic.Turma).filter(academic.Turma.id == turma_id).first()

def get_turmas(db: Session, skip: int = 0, limit: int = 10):
    return db.query(academic.Turma).offset(skip).limit(limit).all()

def create_turma(db: Session, turma_schema: turma.TurmaCreate):
    db_turma = academic.Turma(**turma_schema.model_dump())
    db.add(db_turma)
    db.commit()
    db.refresh(db_turma)
    return db_turma
