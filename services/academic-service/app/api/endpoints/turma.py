from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.database import get_db
from app.schemas import turma as turma_schema
from app.crud import turma as turma_crud

router = APIRouter()

@router.post("/", response_model=turma_schema.TurmaResponse, status_code=201)
def criar_turma(turma: turma_schema.TurmaCreate, db: Session = Depends(get_db)):
    # Futuramente, podemos validar se disciplina_id e professor_id existem no banco!
    return turma_crud.create_turma(db=db, turma_schema=turma)

@router.get("/", response_model=list[turma_schema.TurmaResponse])
def listar_turmas(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return turma_crud.get_turmas(db, skip=skip, limit=limit)
