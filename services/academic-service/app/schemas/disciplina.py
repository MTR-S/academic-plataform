from pydantic import BaseModel

# 1. Schema Base: Atributos comuns
class DisciplinaBase(BaseModel):
    codigo: str  # Ex: "IFCE001"
    nome: str    # Ex: "Engenharia de Software"
    carga_horaria: int

# 2. Schema de Criação (POST)
class DisciplinaCreate(DisciplinaBase):
    pass

# 3. Schema de Resposta (GET)
class DisciplinaResponse(DisciplinaBase):
    id: int

    class Config:
        from_attributes = True
