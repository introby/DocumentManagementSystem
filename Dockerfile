FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine as builder
ADD . /src
WORKDIR /src
RUN chmod +x mvnw && ./mvnw package -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine
ENV JAR_FILE_NAME="dms-0.0.1-SNAPSHOT.jar"
EXPOSE 8080
RUN mkdir /app
COPY --from=builder /src/target/$JAR_FILE_NAME /app/doc-app.jar
ENTRYPOINT ["java", "-jar","/app/doc-app.jar"]