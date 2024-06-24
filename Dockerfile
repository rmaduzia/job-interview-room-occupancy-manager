FROM openjdk:17-jdk-alpine
MAINTAINER rmaduzia
COPY target/room-occupancy-manager-0.0.1-SNAPSHOT.jar room-occupancy-manager-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/room-occupancy-manager-0.0.1-SNAPSHOT.jar"]