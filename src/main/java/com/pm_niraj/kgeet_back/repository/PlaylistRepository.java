package com.pm_niraj.kgeet_back.repository;

import com.pm_niraj.kgeet_back.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByName(String name);
}
