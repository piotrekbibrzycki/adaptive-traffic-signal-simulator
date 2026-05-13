FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
COPY simulator-core/build.gradle.kts simulator-core/build.gradle.kts
COPY simulator-cli/build.gradle.kts simulator-cli/build.gradle.kts

RUN chmod +x gradlew
RUN ./gradlew --no-daemon :simulator-cli:dependencies

COPY simulator-core/src simulator-core/src
COPY simulator-cli/src simulator-cli/src

RUN ./gradlew --no-daemon :simulator-cli:installDist

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/simulator-cli/build/install/simulator-cli ./

ENTRYPOINT ["/app/bin/simulator-cli"]
