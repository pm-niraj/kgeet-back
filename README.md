# kgeet-back

## Overview
**kgeet-back** is a backend service that enables downloading audio from given URLs (using `yt-dlp`), storing the audio files on the server, and streaming them to users. It leverages **Redis** as a caching layer to enable smooth and efficient audio playback by caching audio segments.

---

## Features
- Download audio from any supported video URL via `yt-dlp`
- Save audio files on the backend server
- Stream audio to clients with support for byte-range requests
- Use Redis to cache audio segments for smoother and faster playback
- Scalable and efficient audio streaming backend for music or podcast apps

---

## Prerequisites
- Docker & Docker Compose installed
- Redis server running (can be local or remote)
- Java 21+ (if running without Docker)
- Network access for downloading from URLs

---

## Getting Started

## Install redis server
Run using redisContainer.sh script

### Run with Docker

Build and run the Docker container:

```bash
docker build -t kgeet-back .
docker run -p 8083:8083 kgeet-back
```
### See open endpoints
<img width="1467" height="841" alt="image" src="https://github.com/user-attachments/assets/ada20b24-2d6f-4a96-9792-e3b1430f8557" />
<img width="1468" height="552" alt="image" src="https://github.com/user-attachments/assets/283a6642-a8d1-454f-a111-7bf36df6ce14" />

### Some demo output

[React App.webm](https://github.com/user-attachments/assets/17e0b5f3-40d9-4be1-955f-40d50eb5aeae)



