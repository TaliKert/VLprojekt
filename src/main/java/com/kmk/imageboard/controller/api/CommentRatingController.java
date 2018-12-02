package com.kmk.imageboard.controller.api;

import com.kmk.imageboard.model.DTO.CommentDTO;
import com.kmk.imageboard.service.CommentRatingService;
import com.kmk.imageboard.service.ImageService;
import com.kmk.imageboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class CommentRatingController {

    @Autowired
    CommentRatingService commentRatingService;

    @Autowired
    UserService userService;

    @Autowired
    ImageService imageService;

    @PostMapping("/image/{id}/rate/{bool}")
    public ResponseEntity rateImageUp(Principal principal, @PathVariable String id, @PathVariable String bool) {
        if (principal == null) {
            return ResponseEntity.badRequest().body("Not logged in");
        }

        commentRatingService.rateImage(
                userService.getUser(principal),
                imageService.getImageById(Long.parseLong(id)),
                Boolean.parseBoolean(bool));
        return ResponseEntity.ok().body("Rating approved");
    }

    @PostMapping(value = "/image/{id}/comment")
    public ResponseEntity commentOnImage(Principal principal, @PathVariable String id, @ModelAttribute CommentDTO comment) {
        if (principal == null) {
            return ResponseEntity.badRequest().body("Not logged in");
        }
        commentRatingService.commentImage(
                userService.getUser(principal),
                imageService.getImageById(Long.parseLong(id)),
                comment.getText());
        return ResponseEntity.ok().body("Comment successful");
    }
}
