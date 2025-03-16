package com.pm_niraj.kgeet_back.repository;

import com.pm_niraj.kgeet_back.model.PlaylistMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, Long> {

    @Query("SELECT COALESCE(MAX(pm.orderIndex), 0) FROM PlaylistMusic pm WHERE pm.playlist.id = :playlistId")
    int findMaxOrderIndexByPlaylistId(@Param("playlistId") Long playlistId);


    @Query("SELECT pm FROM PlaylistMusic pm WHERE pm.playlist.name = :playlistName")
    List<PlaylistMusic> findAllByPlaylistName(@Param("playlistName") String playlistName);
}
