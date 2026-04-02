# Etapa 1: Build
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copiar wrapper y código
COPY mvnw ./
COPY mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml ./
COPY src ./src

# Dar permisos al wrapper
RUN chmod +x mvnw

# Construir el jar
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
