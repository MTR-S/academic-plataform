from pydantic import BaseModel

class MatriculaBase(BaseModel):
    aluno_id: int
    turma_id: int
    status: str = "Ativa"

class MatriculaCreate(MatriculaBase):
    pass

class MatriculaResponse(MatriculaBase):
    id: int
    class Config:
        from_attributes = True
