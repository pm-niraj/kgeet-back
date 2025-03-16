package com.pm_niraj.kgeet_back;

import com.pm_niraj.kgeet_back.model.Music;
import com.pm_niraj.kgeet_back.model.Playlist;
import com.pm_niraj.kgeet_back.service.MusicService;
import com.pm_niraj.kgeet_back.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KgeetBackApplication implements CommandLineRunner {

	@Autowired
	private MusicService musicService;
	@Autowired
	private PlaylistService playlistService;

	@Override
	public void run(String... args) throws Exception {
		Music m1 = musicService.createMusic("Kehi Mitho", "https://www.youtube.com/watch?v=D-mERpuyZV8");
		Music m2 = musicService.createMusic("Malai nasodha", "https://www.youtube.com/watch?v=D-mERpuyZV8");
//		Playlist playlist = playlistService.save("myPlaylist");

//		playlistService.addMusicToPlaylist(playlist, m1);
//		playlistService.addMusicToPlaylist(playlist, m2);
	}

	public static void main(String[] args) {
		SpringApplication.run(KgeetBackApplication.class, args);
	}
}
