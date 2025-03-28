package com.pm_niraj.kgeet_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SongUpdateService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendUpdate(String status) {
        messagingTemplate.convertAndSend("/topic/song-updates", status);
    }
}
