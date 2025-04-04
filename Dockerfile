FROM eclipse-temurin:21

COPY ./target/library-0.0.1-SNAPSHOT.jar library-0.0.1-SNAPSHOT.jar

EXPOSE 3000
ENTRYPOINT ["java", "-jar", "library-0.0.1-SNAPSHOT.jar"]
