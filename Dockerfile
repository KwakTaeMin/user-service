FROM amazoncorretto:21 AS build
WORKDIR /user-service
COPY . .
COPY .env .env
RUN ./gradlew build --no-daemon

FROM amazoncorretto:21
WORKDIR /user-service
COPY --from=build /user-service/build/libs/user-service-0.0.1.jar app.jar
COPY --from=build /user-service/.env .env
ENTRYPOINT ["sh", "-c", "export $(grep -v '^#' .env | xargs) && java -jar app.jar"]