from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.database import get_db
from app.schemas import matricula as mat_schema
from app.crud import matricula as mat_crud

router = APIRouter()

@router.post("/", response_model=mat_schema.MatriculaResponse, status_code=201)
def criar_matricula(matricula: mat_schema.MatriculaCreate, db: Session = Depends(get_db)):
    return mat_crud.create_matricula(db=db, matricula_schema=matricula)

@router.get("/", response_model=list[mat_schema.MatriculaResponse])
def listar_matriculas(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return mat_crud.get_matriculas(db, skip=skip, limit=limit)
