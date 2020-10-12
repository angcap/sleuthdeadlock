FROM maven:3.6.3-jdk-8-slim AS build  
RUN mkdir /usr/src/app
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
#COPY ./settings.xml /usr/src/app/settings.xml
#RUN mvn -f /usr/src/app/pom.xml -s /usr/src/app/settings.xml clean package -DskipTests -P localdev
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
EXPOSE 8080
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=8080","-Dspring.profiles.active=openshift","-jar","/usr/app/app.jar"]
