from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

# Importamos a injeção de dependência do banco, os schemas e o crud
from app.db.database import get_db
from app.schemas import aluno as aluno_schema
from app.crud import aluno as aluno_crud

# O Router é como se fosse um mini-aplicativo FastAPI focado só em Alunos
router = APIRouter()

# Rota para CADASTRAR um aluno (Método POST)
@router.post("/", response_model=aluno_schema.AlunoResponse, status_code=201)
def criar_novo_aluno(aluno: aluno_schema.AlunoCreate, db: Session = Depends(get_db)):
    # 1. Regra de Negócio: Verifica se a matrícula já existe no banco
    db_aluno = aluno_crud.get_aluno_by_matricula(db, matricula=aluno.matricula)
    if db_aluno:
        # Se existir, a API barra a requisição com um Erro 400 antes de tentar salvar
        raise HTTPException(status_code=400, detail="Matrícula já cadastrada no sistema.")
    
    # 2. Se estiver tudo certo, manda o CRUD salvar e já devolve a resposta formatada
    return aluno_crud.create_aluno(db=db, aluno_schema=aluno)


# Rota para BUSCAR um aluno específico pelo ID (Método GET)
@router.get("/{aluno_id}", response_model=aluno_schema.AlunoResponse)
def buscar_aluno_por_id(aluno_id: int, db: Session = Depends(get_db)):
    # 1. Pede para o CRUD ir no banco buscar o ID
    db_aluno = aluno_crud.get_aluno(db, aluno_id=aluno_id)
    
    # 2. Se o aluno não existir, devolve o famoso Erro 404
    if db_aluno is None:
        raise HTTPException(status_code=404, detail="Aluno não encontrado.")
    
    return db_aluno