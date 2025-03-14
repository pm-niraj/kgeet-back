package com.pm_niraj.kgeet_back;

import com.pm_niraj.kgeet_back.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KgeetBackApplication implements CommandLineRunner {

	@Autowired
	private MusicService musicService;

	@Override
	public void run(String... args) throws Exception {
		createAllMusic();
	}

	private void createAllMusic() {
		musicService.createMusic("Kehi Mitho", "https://www.youtube.com/watch?v=D-mERpuyZV8");
		musicService.createMusic("Malai nasodha", "https://www.youtube.com/watch?v=D-mERpuyZV8");
		musicService.createMusic("Jhareko paat jhai", "https://www.youtube.com/watch?v=D-mERpuyZV8");
		musicService.createMusic("Jun ko jyoti", "https://www.youtube.com/watch?v=D-mERpuyZV8");
		System.out.println(musicService.total() + " musics created");
	}

	public static void main(String[] args) {
		SpringApplication.run(KgeetBackApplication.class, args);
	}
}
