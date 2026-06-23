from pathlib import Path
from pydantic_settings import BaseSettings, SettingsConfigDict

# Resolve o caminho do .env a partir da localização deste arquivo,
# independente do sistema operacional ou usuário que executar o projeto.
# config.py: services/academic-service/app/core/config.py
# parents[4]: raiz do repositório
_ENV_FILE = Path(__file__).resolve().parents[4] / "infra" / ".env"

class Settings(BaseSettings):
    MYSQL_ROOT_PASSWORD: str
    MYSQL_ACADEMIC_DB: str

    DB_HOST: str = "localhost"
    DB_PORT: str = "3308"
    DB_USER: str = "root"

    @property
    def DATABASE_URL(self) -> str:
        return f"mysql+pymysql://{self.DB_USER}:{self.MYSQL_ROOT_PASSWORD}@{self.DB_HOST}:{self.DB_PORT}/{self.MYSQL_ACADEMIC_DB}"

    model_config = SettingsConfigDict(
        env_file=_ENV_FILE,
        env_file_encoding="utf-8",
        extra="ignore",
    )

settings = Settings()