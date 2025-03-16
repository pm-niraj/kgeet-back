package com.pm_niraj.kgeet_back.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Music {
    public Music() {
        this(null, null);
    }
    public Music(String title, String url){
        this.title = title;
        this.url = url;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Integer id;

    @Column
    private String title;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Column
    private String url;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Column
    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @OneToMany(mappedBy = "music", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlaylistMusic> playlists = new HashSet<>();

    public Set<PlaylistMusic> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<PlaylistMusic> playlists) {
        this.playlists = playlists;
    }
}
