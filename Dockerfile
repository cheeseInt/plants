FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY plants-core/target/plants-core-*.jar core.jar
COPY care-ui/target/care-ui-*.jar ui.jar
