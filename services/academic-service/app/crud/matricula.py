from sqlalchemy.orm import Session
from app.models import academic
from app.schemas import matricula

def get_matriculas(db: Session, skip: int = 0, limit: int = 10):
    return db.query(academic.Matricula).offset(skip).limit(limit).all()

def create_matricula(db: Session, matricula_schema: matricula.MatriculaCreate):
    db_matricula = academic.Matricula(**matricula_schema.model_dump())
    db.add(db_matricula)
    db.commit()
    db.refresh(db_matricula)
    return db_matricula
