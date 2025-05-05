FROM bellsoft/liberica-runtime-container:jdk-17-stream-musl as builder
WORKDIR exploded

ARG JAVA_FILE=target/*.jar

COPY ${JAVA_FILE} application.jar

RUN unzip application.jar

FROM bellsoft/liberica-runtime-container:jdk-17-stream-musl

# Configuration AWT headless et propriétés système
ENV JAVA_OPTS="-Djava.awt.headless=true"

ARG PROJECT_ID

COPY --from=builder /exploded/BOOT-INF/lib /exploded/lib
COPY --from=builder /exploded/META-INF /exploded/META-INF
COPY --from=builder /exploded/BOOT-INF/classes /exploded

RUN echo '{"projectId": "'${PROJECT_ID}'", "java_version": "'$(java -version 2>&1 | tail -1)'"}' > /external-info.json

ENTRYPOINT ["java", "-XX:+AlwaysActAsServerClassMachine", "-Djava.awt.headless=true", "-cp","/exploded:/exploded/lib/*:/external-info.json","com.drloveapp.drloveapp_backend.DrloveappBackendApplication"]