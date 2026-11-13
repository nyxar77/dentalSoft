# stage 1
FROM maven:3-eclipse-temurin-25 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn --batch-mode dependency:go-offline

COPY src ./src
RUN mvn --batch-mode package \
    && mvn --batch-mode dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/lib

#stage 2
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/DentalSoft-1.0-SNAPSHOT.jar ./app.jar
COPY --from=build /app/target/lib ./lib

ENTRYPOINT ["java", "-cp", "app.jar:lib/*", "ma.dentalSoft.DentalSoftApp"]
