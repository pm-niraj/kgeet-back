FROM eclipse-temurin:21-alpine-3.21
WORKDIR /app
COPY . .
# Copy the entire project
EXPOSE 8082
CMD ["./mvnw", "spring-boot:run"]
# Run in development mode
