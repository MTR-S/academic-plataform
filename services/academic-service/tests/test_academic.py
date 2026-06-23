from fastapi.testclient import TestClient
from app.main import app
import time

client = TestClient(app)

# Usamos um timestamp para gerar matrículas e códigos únicos toda vez que o teste rodar
timestamp = str(int(time.time()))

def test_fluxo_completo_microsservico():
    # 1. Cria o Professor
    resp_prof = client.post("/api/professores/", json={
        "siape": f"SP-{timestamp}",
        "departamento": "Engenharia de Computação",
        "usuario_id": int(time.time())
    })
    assert resp_prof.status_code == 201
    prof_id = resp_prof.json()["id"]

    # 2. Cria a Disciplina
    resp_disc = client.post("/api/disciplinas/", json={
        "codigo": f"ENG-{timestamp}",
        "nome": "Sistemas Embarcados e IoT",
        "carga_horaria": 60
    })
    assert resp_disc.status_code == 201
    disc_id = resp_disc.json()["id"]

    # 3. Cria a Turma (Amarrando os dois)
    resp_turma = client.post("/api/turmas/", json={
        "semestre": "2026.1",
        "codigo": f"T01-{timestamp}",
        "disciplina_id": disc_id,
        "professor_id": prof_id
    })
    assert resp_turma.status_code == 201
    turma_id = resp_turma.json()["id"]

    # 4. Cria o Aluno
    resp_aluno = client.post("/api/alunos/", json={
        "matricula": f"MAT-{timestamp}",
        "curso": "Engenharia de Computação",
        "usuario_id": int(time.time()) + 1
    })
    assert resp_aluno.status_code == 201
    aluno_id = resp_aluno.json()["id"]

    # 5. Efetiva a Matrícula
    resp_matricula = client.post("/api/matriculas/", json={
        "aluno_id": aluno_id,
        "turma_id": turma_id,
        "status": "Ativa"
    })
    assert resp_matricula.status_code == 201