from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    # O Pydantic vai exigir que essas variáveis existam no seu .env
    MYSQL_ROOT_PASSWORD: str
    MYSQL_ACADEMIC_DB: str
    
    # Credenciais de acesso local
    DB_HOST: str = "localhost"
    DB_PORT: str = "3308"
    DB_USER: str = "root"

    @property
    def DATABASE_URL(self) -> str:
        # Monta a string exata que o SQLAlchemy precisa para conectar no MySQL
        return f"mysql+pymysql://{self.DB_USER}:{self.MYSQL_ROOT_PASSWORD}@{self.DB_HOST}:{self.DB_PORT}/{self.MYSQL_ACADEMIC_DB}"

    # Aponta para onde o seu arquivo .env está fisicamente no projeto
    model_config = SettingsConfigDict(env_file="/home/vinicius/Área de Trabalho/faculdade/S6/ENG. SOFTWARE/academic-plataform/infra/.env", extra="ignore")

# Instancia as configurações para serem importadas pelo database.py
settings = Settings()