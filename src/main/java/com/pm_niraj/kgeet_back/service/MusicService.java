package com.pm_niraj.kgeet_back.service;

import com.pm_niraj.kgeet_back.model.Music;
import com.pm_niraj.kgeet_back.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicService {
    private MusicRepository musicRepository;
    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public Music createMusic(String title, String url, String artist){
        Music music = new Music(title, url, artist);
        return musicRepository.saveAndFlush(music);
    }

    public long total(){
        return musicRepository.count();
    }
}
