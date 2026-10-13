# ============================================================
# Dockerfile - Multi-Stage Build Spring Boot Java 21
# ============================================================

# Estagio 1: Compilacao com Maven e Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia o arquivo de configuracao do Maven
COPY pom.xml .

# Baixa as dependencias e aproveita o cache Docker
RUN mvn dependency:go-offline -B

# Copia o codigo-fonte
COPY src ./src

# Compila e gera o JAR executavel
RUN mvn clean package -DskipTests -B

# Estagio 2: Execucao com Java 21
FROM eclipse-temurin:21-jre-alpine AS runner

# Cria usuario sem privilegios de administrador
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copia o JAR gerado
COPY --from=builder /app/target/*.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]