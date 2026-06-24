from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import text
import logging
from app.db.database import get_db

router = APIRouter()
logger = logging.getLogger(__name__)

@router.get("/health", tags=["Monitoramento"])
def check_health(db: Session = Depends(get_db)):
    try:
        # Faz um "ping" super rápido no banco de dados
        db.execute(text("SELECT 1"))
        logger.info("Health check executado: Serviço UP e Banco de dados UP.")
        
        return {
            "status": "UP",
            "database": "UP",
            "service": "academic-service"
        }
    except Exception as e:
        logger.error(f"Health check falhou. Erro de banco de dados: {str(e)}")
        # Se o banco cair, o serviço retorna erro 503 (Serviço Indisponível)
        raise HTTPException(status_code=503, detail="Database connection failed")