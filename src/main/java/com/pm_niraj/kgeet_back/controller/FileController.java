package com.pm_niraj.kgeet_back.controller;
import com.pm_niraj.kgeet_back.utilities.AudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;

import reactor.core.publisher.Flux;

@RestController
public class FileController {
    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        File file = new File("uploads/" + filename);

        if (file.exists()) {
            // Check the file extension and set the correct media type
            String fileExtension = filename.substring(filename.lastIndexOf("."));
            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;  // Default for binary files

            if (".mp3".equalsIgnoreCase(fileExtension)) {
                mediaType = MediaType.valueOf("audio/mpeg");
            }

            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .contentType(mediaType) // Set the content type
                    .body(resource);  // Return the resource as the response body
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if the file doesn't exist
        }
    }

    @GetMapping("/audio_length/{filename}")
    public ResponseEntity<Long> getAudioFileLength(@PathVariable String filename) throws IOException {
        Path audioPath = Paths.get("uploads", filename); // change path as needed

        if (!Files.exists(audioPath)) {
            return ResponseEntity.notFound().build();
        }

        long contentLength = Files.size(audioPath);
        return ResponseEntity.ok(contentLength);
    }
    @GetMapping("/serve/{filename}")
    public ResponseEntity<byte[]> serveAudio(@PathVariable String filename,
                                             @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        String cacheKey = "audio:" + filename;

        byte[] fullFileBytes;

        // 1. Check cache
        if (redisTemplate.hasKey(cacheKey)) {
            Object cached = redisTemplate.opsForValue().get(cacheKey);

            if (cached instanceof byte[]) {
                fullFileBytes = (byte[]) cached;
            } else if (cached instanceof String) {
                // If the data was stored as a Base64 encoded string, decode it
                fullFileBytes = Base64.getDecoder().decode((String) cached);
            } else {
                // Handle unexpected types
                throw new IllegalStateException("Cached value is neither byte[] nor Base64 encoded string");
            }
        } else {
            // 2. Load from file system if not cached
            File file = new File("uploads/" + filename);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            fullFileBytes = Files.readAllBytes(file.toPath());

            // 3. Cache the entire file
            redisTemplate.opsForValue().set(cacheKey, fullFileBytes);
        }

        int fileLength = fullFileBytes.length;
        int start = 0;
        int end = fileLength - 1;


// 4. Parse range header if exists
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            try {
                start = Integer.parseInt(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    end = Integer.parseInt(ranges[1]);
                } else {
                    end = fileLength - 1; // Handle open-ended range like bytes=4000-
                }
            } catch (NumberFormatException ignored) {
            }

            // Clamp end to fileLength - 1
            end = Math.min(end, fileLength - 1);
        }


        // 5. Validate range
        if (start > end || start >= fileLength) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .header("Content-Range", "bytes */" + fileLength)
                    .build();
        }

        // 6. Serve partial content
        byte[] partialBytes = Arrays.copyOfRange(fullFileBytes, start, end + 1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        headers.set("Accept-Ranges", "bytes");
        headers.setContentLength(partialBytes.length);
        headers.set("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);

        HttpStatus status = (rangeHeader != null) ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;

        return new ResponseEntity<>(partialBytes, headers, status);
    }


    @GetMapping("/duration/{filename}")
    public ResponseEntity<Double> getAudioDuration(@PathVariable String filename) {
        String cacheKey = "audio:duration:" + filename;

        Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
        if (cachedValue != null) {
            try {
                if (cachedValue instanceof String) {
                    return ResponseEntity.ok(Double.parseDouble((String) cachedValue));
                } else if (cachedValue instanceof byte[]) {
                    String decoded = new String((byte[]) cachedValue, StandardCharsets.UTF_8);
                    return ResponseEntity.ok(Double.parseDouble(decoded));
                } else {
                    System.err.println("Unexpected Redis value type: " + cachedValue.getClass());
                }
            } catch (Exception e) {
                System.err.println("Failed to parse cached duration: " + e.getMessage());
                // fallback to recalculate
            }
        }

        // Load file from disk
        File file = new File("uploads/" + filename);
        if (!file.exists()) return ResponseEntity.notFound().build();

        double duration = AudioUtils.getDurationWithFFmpeg(file);
        if (duration < 0) return ResponseEntity.status(500).build();

        // Save as String to Redis
        redisTemplate.opsForValue().set(cacheKey, String.valueOf(duration).getBytes());

        return ResponseEntity.ok(duration);
    }

}
