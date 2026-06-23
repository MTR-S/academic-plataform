from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.db.database import get_db
from app.schemas import disciplina as disciplina_schema
from app.crud import disciplina as disciplina_crud

router = APIRouter()

# Rota para CADASTRAR uma disciplina
@router.post("/", response_model=disciplina_schema.DisciplinaResponse, status_code=201)
def criar_nova_disciplina(disciplina: disciplina_schema.DisciplinaCreate, db: Session = Depends(get_db)):
    db_disciplina = disciplina_crud.get_disciplina_by_codigo(db, codigo=disciplina.codigo)
    if db_disciplina:
        raise HTTPException(status_code=400, detail="Código de disciplina já cadastrado.")
    return disciplina_crud.create_disciplina(db=db, disciplina_schema=disciplina)

# Rota para BUSCAR uma disciplina por ID
@router.get("/{disciplina_id}", response_model=disciplina_schema.DisciplinaResponse)
def buscar_disciplina_por_id(disciplina_id: int, db: Session = Depends(get_db)):
    db_disciplina = disciplina_crud.get_disciplina(db, disciplina_id=disciplina_id)
    if db_disciplina is None:
        raise HTTPException(status_code=404, detail="Disciplina não encontrada.")
    return db_disciplina

# Rota para LISTAR as disciplinas
@router.get("/", response_model=list[disciplina_schema.DisciplinaResponse])
def listar_disciplinas(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    return disciplina_crud.get_disciplinas(db, skip=skip, limit=limit)
