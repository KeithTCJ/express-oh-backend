package com.team_one.expressoh.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api/admin/upload")
@CrossOrigin(origins = "*") // You can secure this further if needed
public class ImageUploadController {

    private static final String DIRECTORY = "uploads/images/";

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            File dir = new File(DIRECTORY);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path filepath = Paths.get(DIRECTORY, filename);
            Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/images/" + filename; // This is what you save to DB later
            return new ResponseEntity<>(imageUrl, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload image.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
