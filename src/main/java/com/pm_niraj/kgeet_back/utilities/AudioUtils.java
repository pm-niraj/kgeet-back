package com.pm_niraj.kgeet_back.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class AudioUtils {

    public static double getDurationWithFFmpeg(File file) {
        try {
            Process process = new ProcessBuilder(
                    "ffprobe", "-v", "error",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    file.getAbsolutePath()
            ).redirectErrorStream(true).start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String durationStr = reader.readLine();
                if (durationStr != null) {
                    return Double.parseDouble(durationStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
