FROM eclipse-temurin:21-alpine-3.21

# Set the working directory
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Expose the application port
EXPOSE 8083

# Run the application in development mode
CMD ["./mvnw", "spring-boot:run"]
