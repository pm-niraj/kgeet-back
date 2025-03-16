package com.pm_niraj.kgeet_back.service;

import com.pm_niraj.kgeet_back.model.Music;
import com.pm_niraj.kgeet_back.model.Playlist;
import com.pm_niraj.kgeet_back.model.PlaylistMusic;
import com.pm_niraj.kgeet_back.repository.PlaylistMusicRepository;
import com.pm_niraj.kgeet_back.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private PlaylistMusicRepository playlistMusicRepository;

    public PlaylistService() {
    }

    public PlaylistService(PlaylistRepository playlistRepository, PlaylistMusicRepository playlistMusicRepository) {
        this.playlistRepository = playlistRepository;
        this.playlistMusicRepository = playlistMusicRepository;
    }

    public Playlist save(String name) {
        Playlist playlist = new Playlist(name);
        return playlistRepository.saveAndFlush(playlist);
    }

    public long total() {
        return playlistRepository.count();
    }

    public void addMusicToPlaylist(Playlist playlist, Music music) {
        int maxOrderIndex = playlistMusicRepository.findMaxOrderIndexByPlaylistId(playlist.getId());
        int nextOrderIndex = maxOrderIndex + 1;

        PlaylistMusic playlistMusic = new PlaylistMusic(playlist, music, nextOrderIndex);
        playlistMusicRepository.save(playlistMusic);
    }
}
