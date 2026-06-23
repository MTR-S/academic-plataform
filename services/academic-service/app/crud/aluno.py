from sqlalchemy.orm import Session
from app.models import academic
from app.schemas import aluno

# 1. READ: Buscar um aluno específico pelo ID (Chave Primária)
def get_aluno(db: Session, aluno_id: int):
    return db.query(academic.Aluno).filter(academic.Aluno.id == aluno_id).first()

# 2. READ: Buscar um aluno pela matrícula (Útil para evitar cadastros duplicados)
def get_aluno_by_matricula(db: Session, matricula: str):
    return db.query(academic.Aluno).filter(academic.Aluno.matricula == matricula).first()

# 3. READ: Listar vários alunos (com paginação para não travar o banco se houver 10.000 alunos)
def get_alunos(db: Session, skip: int = 0, limit: int = 10):
    return db.query(academic.Aluno).offset(skip).limit(limit).all()

# 4. CREATE: Recebe os dados validados do Pydantic e salva no SQLAlchemy
def create_aluno(db: Session, aluno_schema: aluno.AlunoCreate):
    # Desempacota os dados do schema da internet e transforma no modelo do banco
    db_aluno = academic.Aluno(
        matricula=aluno_schema.matricula,
        curso=aluno_schema.curso,
        usuario_id=aluno_schema.usuario_id
    )
    
    # Prepara o comando INSERT
    db.add(db_aluno)
    # Executa o comando e salva de fato no MySQL
    db.commit()
    # Atualiza o objeto do Python com os dados novos do banco (como o ID gerado)
    db.refresh(db_aluno)
    
    return db_aluno