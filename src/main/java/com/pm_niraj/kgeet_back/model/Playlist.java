package com.pm_niraj.kgeet_back.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Playlist {

    public Playlist() {
    }

    public Playlist(String name) {
        this.name = name;
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

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlaylistMusic> musics = new HashSet<>();

    public Set<PlaylistMusic> getMusics() {
        return musics;
    }

    public void setMusics(Set<PlaylistMusic> musics) {
        this.musics = musics;
    }
}
