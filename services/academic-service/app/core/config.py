from pathlib import Path
from pydantic_settings import BaseSettings, SettingsConfigDict

def _resolve_env_file() -> str | None:
    # Dentro do Docker o código fica em /app — não há 4 níveis acima.
    # O IndexError é capturado para evitar crash; nesse caso o docker-compose
    # injeta as variáveis diretamente como env vars.
    try:
        candidate = Path(__file__).resolve().parents[4] / "infra" / ".env"
        return str(candidate) if candidate.is_file() else None
    except IndexError:
        return None

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
        env_file=_resolve_env_file(),
        env_file_encoding="utf-8",
        extra="ignore",
    )

settings = Settings()