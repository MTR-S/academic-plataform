from sqlalchemy.orm import Session
from app.models import academic
from app.schemas import professor

def get_professor(db: Session, professor_id: int):
    return db.query(academic.Professor).filter(academic.Professor.id == professor_id).first()

def get_professor_by_siape(db: Session, siape: str):
    return db.query(academic.Professor).filter(academic.Professor.siape == siape).first()

def get_professores(db: Session, skip: int = 0, limit: int = 10):
    return db.query(academic.Professor).offset(skip).limit(limit).all()

def create_professor(db: Session, professor_schema: professor.ProfessorCreate):
    db_professor = academic.Professor(**professor_schema.model_dump())
    db.add(db_professor)
    db.commit()
    db.refresh(db_professor)
    return db_professor
