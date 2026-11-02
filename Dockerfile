# syntax=docker/dockerfile:1
FROM node:18-alpine AS frontend
WORKDIR /frontend
RUN npm install --global pnpm@8.15.9
COPY backend/src/main/resources/frontend/package.json backend/src/main/resources/frontend/pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY backend/src/main/resources/frontend/ ./
RUN pnpm build

FROM eclipse-temurin:17-jdk AS backend
WORKDIR /build
COPY backend/ ./
COPY --from=frontend /frontend/dist/ ./src/main/resources/frontend/dist/
RUN chmod +x gradlew && ./gradlew --no-daemon bootJar -x prebuild

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend /build/build/libs/*-SNAPSHOT.jar /app/app.jar
USER 1000:1000
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
