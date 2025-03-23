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
            String fileName = UUID.randomUUID() + ".mp3";
            String filePath = OUTPUT_DIR + fileName;

            // Build the yt-dlp command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "yt-dlp", "-f", "bestaudio", "--extract-audio",
                    "--audio-format", "mp3", "-o", filePath, musicDto.getAudioUrl()
            );

            // Redirect errors to avoid blocking
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the command output (optional)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("File saved: " + filePath);
            } else {
                System.out.println("Error processing video.");
            }
            Music music = musicService.createMusic(musicDto.getTitle(), fileName);
            return new MusicDto(music);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to process the request.");
            return new MusicDto();
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