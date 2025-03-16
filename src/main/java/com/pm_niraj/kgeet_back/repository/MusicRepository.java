package com.pm_niraj.kgeet_back.repository;

import com.pm_niraj.kgeet_back.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface MusicRepository extends JpaRepository<Music, Integer> {

}
