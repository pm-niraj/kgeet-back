# Use Maven base image with Java 21 and Alpine
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Set working directory inside container
WORKDIR /app

# Install Python, pip, ffmpeg, and yt-dlp
RUN apk add --no-cache python3 py3-pip curl ffmpeg \
    && python3 -m venv /opt/venv \
    && /opt/venv/bin/pip install yt-dlp

# Make venv available to PATH
ENV PATH="/opt/venv/bin:$PATH"

# Copy all files into container
COPY . .

# Ensure Maven wrapper is executable
RUN chmod +x mvnw

# Expose application port
EXPOSE 8083

# Default command to run the Spring Boot app
CMD ["./mvnw", "spring-boot:run"]

