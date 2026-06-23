import pytest
from app.db.database import Base, engine
from app.models import academic  # noqa: F401 — registers all models with Base.metadata


@pytest.fixture(scope="session", autouse=True)
def create_tables():
    Base.metadata.create_all(bind=engine)
    yield
    Base.metadata.drop_all(bind=engine)
