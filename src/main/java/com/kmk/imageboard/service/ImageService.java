package com.kmk.imageboard.service;

import com.kmk.imageboard.model.Image;
import com.kmk.imageboard.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserService userService;

    public ResponseEntity<String> uploadImage(Principal principal, MultipartFile file) throws IOException {
        if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
            return ResponseEntity.unprocessableEntity().body("Bad file type!");
        }
        Long uploaderId = userService.getUser(principal).getId();
        Image newEntity = imageRepository.save(new Image(uploaderId));
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get("imagerepository", String.valueOf(newEntity.getId())), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok().body("Image uploaded!");
        }
    }
}
