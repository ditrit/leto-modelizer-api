# Build stage
FROM gradle:8.5.0-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21
COPY --from=build /app/build/libs/leto-modelizer-api-0.0.1-SNAPSHOT.jar .
EXPOSE 8443
ENTRYPOINT ["java","-jar","/leto-modelizer-api-0.0.1-SNAPSHOT.jar"]
