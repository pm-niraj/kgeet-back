from flask import Flask, request
from flask_cors import CORS  # Import Flask-CORS
import subprocess

app = Flask(__name__)

# Enable CORS for all routes and all domains
CORS(app, origins=["http://localhost:8083"])  # Replace with your Spring Boot frontend URL

# This endpoint will trigger yt-dlp to download the audio
@app.route('/download', methods=['POST'])
def download():
    url = request.json.get('url')
    title = request.json.get('title')
    if not url:
        return "URL is required", 400

    # Define the output directory where the audio will be stored (from shared volume)
    output_path = "/downloads/" + title + ".%(ext)s"  # This corresponds to the shared volume /uploads
    try:
        # Run yt-dlp to download audio
        subprocess.run(
            ["yt-dlp", "-f", "bestaudio", "--extract-audio", "--audio-format", "mp3", "-o", output_path, url],
            check=True
        )
        return "Downloaded the mp3", 200
    except subprocess.CalledProcessError:
        return "Error starting download", 500

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)  # Expose the Flask app on port 5000
