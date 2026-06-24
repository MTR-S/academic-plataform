# Usa a mesma versão do Python que você tem na sua máquina
FROM python:3.12-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o arquivo de requisitos e instala as dependências
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copia todo o código do seu microsserviço para dentro do contêiner
COPY . .

# Expõe a porta 8000 (onde o FastAPI vai rodar)
EXPOSE 8000

# Comando para ligar o servidor quando o contêiner iniciar
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]