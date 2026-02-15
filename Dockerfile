FROM eclipse-temurin:21-jdk
COPY target/*.jar AirLineReservation-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/AirLineReservation-0.0.1-SNAPSHOT.jar"]
