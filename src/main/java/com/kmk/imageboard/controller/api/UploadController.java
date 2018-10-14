package com.kmk.imageboard.controller.api;

import com.kmk.imageboard.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
public class UploadController {

    @Autowired
    ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(Principal principal, @RequestParam("file") MultipartFile file) throws IOException {
        return imageService.uploadImage(principal, file);
    }


}
