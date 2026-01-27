# Docker Build Notes

## Local Docker Build

If you encounter SSL certificate issues when building Docker locally, you can:

1. **Use the pre-built JAR**:
   ```bash
   # Build JAR locally first
   mvn clean package
   
   # Then use this simpler Dockerfile
   FROM eclipse-temurin:17-jre-alpine
   WORKDIR /app
   COPY target/ultimate-boxing-game-*.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

2. **Pull from GitHub Container Registry**:
   ```bash
   docker pull ghcr.io/prattak86/ultimate-boxing-game:latest
   docker run -p 8080:8080 ghcr.io/prattak86/ultimate-boxing-game:latest
   ```

3. **Use docker-compose** (recommended):
   ```bash
   docker-compose up
   ```

## CI/CD Build

The GitHub Actions workflow builds Docker images successfully in the cloud environment and publishes them to GitHub Container Registry (GHCR).
