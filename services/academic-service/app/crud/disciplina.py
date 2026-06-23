from sqlalchemy.orm import Session
from app.models import academic
from app.schemas import disciplina

# 1. READ: Buscar uma disciplina pelo ID
def get_disciplina(db: Session, disciplina_id: int):
    return db.query(academic.Disciplina).filter(academic.Disciplina.id == disciplina_id).first()

# 2. READ: Buscar pelo código da disciplina (evita duplicados)
def get_disciplina_by_codigo(db: Session, codigo: str):
    return db.query(academic.Disciplina).filter(academic.Disciplina.codigo == codigo).first()

# 3. READ: Listar disciplinas com paginação
def get_disciplinas(db: Session, skip: int = 0, limit: int = 10):
    return db.query(academic.Disciplina).offset(skip).limit(limit).all()

# 4. CREATE: Salva uma nova disciplina no banco
def create_disciplina(db: Session, disciplina_schema: disciplina.DisciplinaCreate):
    db_disciplina = academic.Disciplina(
        codigo=disciplina_schema.codigo,
        nome=disciplina_schema.nome,
        carga_horaria=disciplina_schema.carga_horaria
    )
    db.add(db_disciplina)
    db.commit()
    db.refresh(db_disciplina)
    return db_disciplina
