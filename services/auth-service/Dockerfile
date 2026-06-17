# Estágio de Execução (Runtime)
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo .jar gerado pelo build do Maven para dentro do container
# O comando 'mvn package' gera o jar dentro da pasta 'target'
COPY target/*.jar app.jar

# Expõe a porta interna que a aplicação escuta
EXPOSE 8080

# Comando para iniciar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]