package com.pm_niraj.kgeet_back.dto;

import com.pm_niraj.kgeet_back.model.Music;
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

    public MusicDto(){

    }
    public MusicDto(String title, String audioUrl) {
        this.title = title;
        this.audioUrl = audioUrl;
    }

    public MusicDto(Music music) {
        this.title = music.getTitle();
        this.audioUrl = music.getUrl();
    }
}
