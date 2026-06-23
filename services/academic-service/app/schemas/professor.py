from pydantic import BaseModel

class ProfessorBase(BaseModel):
    siape: str
    departamento: str   # <-- A Alfândega agora exige a string do departamento
    usuario_id: int

class ProfessorCreate(ProfessorBase):
    pass

class ProfessorResponse(ProfessorBase):
    id: int
    class Config:
        from_attributes = True
