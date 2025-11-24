FROM maven:3.9-eclipse-temurin-21-noble as build
WORKDIR /workspace/app

COPY domain/pom.xml domain/pom.xml
COPY application/pom.xml application/pom.xml
COPY infrastructure/pom.xml infrastructure/pom.xml
COPY presentation/pom.xml presentation/pom.xml
COPY main/pom.xml main/pom.xml
COPY pom.xml pom.xml

RUN mvn dependency:go-offline

COPY domain/src domain/src
COPY application/src application/src
COPY infrastructure/src infrastructure/src
COPY presentation/src presentation/src
COPY main/src main/src

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-ubi9-minimal
ARG DEPENDENCY=/workspace/app/main/target

WORKDIR /app
COPY --from=build ${DEPENDENCY}/lib ./lib
COPY --from=build ${DEPENDENCY}/cleanspace-api.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "cleanspace-api.jar"]