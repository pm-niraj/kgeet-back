package com.pm_niraj.kgeet_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class AudioService {

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    public byte[] getOrCacheAudio(String filename) throws IOException {
        byte[] cachedData = redisTemplate.opsForValue().get(filename);
        if (cachedData != null) {
            return cachedData;
        }

        // Load file from disk
        File file = new File("uploads/" + filename);
        if (!file.exists()) return null;

        byte[] data = Files.readAllBytes(file.toPath());

        // Cache in Redis
        redisTemplate.opsForValue().set(filename, data);
        return data;
    }
}
