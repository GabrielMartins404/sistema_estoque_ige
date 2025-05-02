# Etapa 1 — Build da aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS build

ENV PROJECT_HOME=/usr/src/estoqueige
WORKDIR $PROJECT_HOME


# Copiar todos os arquivos para dentro do container
COPY . .

# Realizar o build (sem testes)
RUN mvn clean package -DskipTests
RUN ls -lh target/
RUN mv target/*.jar estoqueige.jar

# Etapa 2 — Imagem final
FROM eclipse-temurin:21-jdk

ENV PROJECT_HOME=/usr/src/estoqueige
WORKDIR $PROJECT_HOME

# Copiar apenas o JAR gerado da etapa anterior
COPY --from=build /usr/src/estoqueige/estoqueige.jar estoqueige.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Define o comando de inicialização do container
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "estoqueige.jar"]
