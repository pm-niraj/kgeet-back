package com.pm_niraj.kgeet_back.controller;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class FileController {

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
}
