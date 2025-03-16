package com.pm_niraj.kgeet_back.model;

import jakarta.persistence.*;

@Entity
public class PlaylistMusic {

    public PlaylistMusic() {}

    public PlaylistMusic(Playlist playlist, Music music, int orderIndex) {
        this.playlist = playlist;
        this.music = music;
        this.orderIndex = orderIndex;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "music_id")
    private Music music;

    private int orderIndex;


    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
