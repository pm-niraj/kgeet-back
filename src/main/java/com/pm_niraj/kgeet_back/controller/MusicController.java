package com.pm_niraj.kgeet_back.controller;

import com.pm_niraj.kgeet_back.dto.MusicDto;
import com.pm_niraj.kgeet_back.model.Music;
import com.pm_niraj.kgeet_back.model.Playlist;
import com.pm_niraj.kgeet_back.repository.MusicRepository;
import com.pm_niraj.kgeet_back.repository.PlaylistMusicRepository;
import com.pm_niraj.kgeet_back.repository.PlaylistRepository;
import com.pm_niraj.kgeet_back.service.MusicService;
import com.pm_niraj.kgeet_back.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/musics")
public class MusicController {

    public static final String OUTPUT_DIR = "uploads/";
    private final MusicRepository musicRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistService playlistService;
    private MusicService musicService;
    private final PlaylistMusicRepository playlistMusicRepository;

    @Autowired
    public MusicController(MusicService musicService, MusicRepository musicRepository, PlaylistRepository playlistRepository, PlaylistMusicRepository playlistMusicRepository, PlaylistService playlistService) {
        this.musicService = musicService;
        this.musicRepository = musicRepository;
        this.playlistRepository = playlistRepository;
        this.playlistMusicRepository = playlistMusicRepository;
        this.playlistService = playlistService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MusicDto createMusic(@RequestBody @Valid MusicDto musicDto) {
        try {
            // Ensure the uploads directory exists
            Files.createDirectories(Paths.get(OUTPUT_DIR));

            // Generate a unique filename
            String fileName = UUID.randomUUID() + "";
            String filePath = OUTPUT_DIR + fileName;

            // The URL for the yt-dlp server (running in a separate container)
            String ytDlpApiUrl = "http://yt-dlp-server:5000/download";  // yt-dlp container URL

            // Create the JSON request payload for the yt-dlp API
            String requestJson = "{\"url\": \"" + musicDto.getAudioUrl() + "\", \"title\" : \"" + fileName + "\"}";

            // Set up the headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            // Use RestTemplate to send a POST request to the yt-dlp server
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(ytDlpApiUrl, entity, String.class);

            // Check if the response indicates success
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Download started successfully!");
            } else {
                System.out.println("Error starting download: " + response.getStatusCode());
            }

            // After successful download, create the Music entity
            Music music = musicService.createMusic(musicDto.getTitle(), fileName);

            // Return the MusicDto object with the created music details
            return new MusicDto(music);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to process the request.");
            return new MusicDto();  // Return an empty MusicDto in case of error
        }
    }

    @GetMapping
    public List<MusicDto> getAllMusics() {
        return musicRepository.findAll().stream().map(MusicDto::new).toList();
    }

    @GetMapping("/{playlist_name}")
    public List<MusicDto> getMusicById(@PathVariable("playlist_name") String playlistName) {
        return playlistMusicRepository.findAllByPlaylistName(playlistName).stream().map(MusicDto::new).toList();
    }

    @PostMapping("/{playlist_name}/{music_id}")
    @ResponseStatus(HttpStatus.OK)
    public MusicDto addMusicToPlaylist(@PathVariable("playlist_name") String playlistName,
                                       @PathVariable("music_id") int musicId) {
        Playlist playlist = playlistRepository.findByName(playlistName).get();
        Music music = musicRepository.findById(musicId).get();
        playlistService.addMusicToPlaylist(playlist, music);
        return new MusicDto(music);
    }

}