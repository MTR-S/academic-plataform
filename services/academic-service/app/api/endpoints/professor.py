from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.database import get_db
from app.schemas import professor as prof_schema
from app.crud import professor as prof_crud

router = APIRouter()

@router.post("/", response_model=prof_schema.ProfessorResponse, status_code=201)
def criar_professor(professor: prof_schema.ProfessorCreate, db: Session = Depends(get_db)):
    if prof_crud.get_professor_by_siape(db, siape=professor.siape):
        raise HTTPException(status_code=400, detail="SIAPE já cadastrado.")
    return prof_crud.create_professor(db=db, professor_schema=professor)

@router.get("/", response_model=list[prof_schema.ProfessorResponse])
def listar_professores(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return prof_crud.get_professores(db, skip=skip, limit=limit)
    
@router.get("/{professor_id}", response_model=prof_schema.ProfessorResponse)
def buscar_professor(professor_id: int, db: Session = Depends(get_db)):
    db_prof = prof_crud.get_professor(db, professor_id)
    if not db_prof:
        raise HTTPException(status_code=404, detail="Professor não encontrado")
    return db_prof
