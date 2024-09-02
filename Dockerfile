FROM amazoncorretto:21 AS build
WORKDIR /user-service
COPY . .
RUN ./gradlew build --no-daemon

FROM amazoncorretto:21
WORKDIR /user-service
COPY --from=build /user-service/build/libs/user-service-0.0.1.jar app.jar
COPY COPY /home/ec2-user/.env .env

ENTRYPOINT ["sh", "-c", "export $(grep -v '^#' .env | xargs) && java -jar app.jar"]
