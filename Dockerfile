FROM eclipse-temurin:21-alpine-3.21

# Set the working directory
WORKDIR /app
# Install yt-dlp and other dependencies
RUN apk add --no-cache python3 py3-pip \
    && python3 -m venv /opt/venv \
    && /opt/venv/bin/pip install yt-dlp

# Add the virtual environment to PATH
ENV PATH="/opt/venv/bin:$PATH"

# Copy Maven dependencies and Spring Boot project
COPY ./pom.xml ./
COPY ./src ./src
# Install Maven (if not already included in the image)
RUN apk add --no-cache maven

# Build the Maven project
RUN mvn clean package -DskipTests

# Expose the application port
EXPOSE 8083

# Run the application in development mode
CMD ["./mvnw", "spring-boot:run"]
