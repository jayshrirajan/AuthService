FROM openjdk:17
COPY build/libs/authservice-0.0.1-SNAPSHOT.jar authservice.jar
ENTRYPOINT ["java","-jar","/authservice.jar"]
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar .
ENV JAVA_TOOL_OPTIONS "-javaagent:./opentelemetry-javaagent.jar"