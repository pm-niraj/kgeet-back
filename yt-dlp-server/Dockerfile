# Use Debian as base image
FROM debian:bullseye-slim

# Set the working directory
WORKDIR /app

# Install dependencies for Python, yt-dlp, and Flask
RUN apt-get update && \
    apt-get install -y \
    python3 \
    python3-pip \
    curl \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

# Install yt-dlp using pip
RUN pip3 install --no-cache-dir yt-dlp Flask flask-cors

# Copy your server files into the container
COPY . .

# Expose the port the Flask app will run on
EXPOSE 5000

# Command to run the Flask server
CMD ["python3", "yt-dlp-server.py"]
