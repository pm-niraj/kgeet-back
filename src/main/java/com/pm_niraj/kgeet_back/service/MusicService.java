package com.pm_niraj.kgeet_back.service;

import com.pm_niraj.kgeet_back.model.Music;
import com.pm_niraj.kgeet_back.repository.MusicRepository;
import org.springframework.stereotype.Service;

@Service
public class MusicService {
    private MusicRepository musicRepository;
    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public Music createMusic(String title, String url){
        Music music = new Music(title, url);
        return musicRepository.save(music);
    }

    public long total(){
        return musicRepository.count();
    }
}
