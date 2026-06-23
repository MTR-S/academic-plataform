from pydantic import BaseModel
from typing import Optional

# 1. Schema Base: Atributos que são comuns tanto na hora de criar quanto na de ler
class AlunoBase(BaseModel):
    matricula: str
    curso: str
    usuario_id: int

# 2. Schema de Criação (POST): Usado quando o Matheus mandar os dados para você.
# Por enquanto é igual ao Base, mas no futuro poderíamos adicionar validações específicas aqui.
class AlunoCreate(AlunoBase):
    pass

# 3. Schema de Resposta (GET): Usado para formatar a saída da API.
# Ele devolve os dados do aluno + o ID que o banco de dados gerou.
class AlunoResponse(AlunoBase):
    id: int

    # Configuração essencial: Ensina o Pydantic a ler objetos do SQLAlchemy
    class Config:
        from_attributes = True