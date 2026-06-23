from pydantic import BaseModel

class TurmaBase(BaseModel):
    codigo: str
    semestre: str
    disciplina_id: int
    professor_id: int

class TurmaCreate(TurmaBase):
    pass

class TurmaResponse(TurmaBase):
    id: int
    class Config:
        from_attributes = True