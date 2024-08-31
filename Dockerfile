FROM amazoncorretto:21 AS build
WORKDIR /user-service
COPY . .
RUN ./gradlew build --no-daemon

FROM amazoncorretto:21
WORKDIR /user-service
COPY --from=build /user-service/build/libs/user-service-0.0.1.jar app.jar

ENTRYPOINT ["sh", "-c", "java -jar app.jar"]
