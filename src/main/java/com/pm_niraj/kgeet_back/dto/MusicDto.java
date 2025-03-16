package com.pm_niraj.kgeet_back.dto;

import com.pm_niraj.kgeet_back.model.Music;
import com.pm_niraj.kgeet_back.model.PlaylistMusic;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MusicDto {
    @Min(0)
    @Max(100)
    private String title;


    @Min(0)
    @Max(100)
    private String audioUrl;

    private String artist;

    public MusicDto(){

    }
    public MusicDto(String title, String audioUrl, String artist) {
        this.title = title;
        this.audioUrl = audioUrl;
        this.artist = artist;
    }

    public MusicDto(Music music) {
        this.title = music.getTitle();
        this.audioUrl = music.getUrl();
        this.artist = music.getArtist();
    }

    public MusicDto(PlaylistMusic playlistMusic) {
        this.title = playlistMusic.getMusic().getTitle();
        this.audioUrl = playlistMusic.getMusic().getUrl();
        this.artist = playlistMusic.getMusic().getArtist();
    }
}
