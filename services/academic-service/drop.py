from app.db.database import engine
from app.models.academic import Turma, Matricula

# 1. Primeiro apagamos a filha (quem tem a chave estrangeira)
Matricula.__table__.drop(engine)

# 2. Agora podemos apagar a mãe livremente
Turma.__table__.drop(engine)

print("💥 Tabelas 'matriculas' e 'turmas' apagadas com sucesso!")