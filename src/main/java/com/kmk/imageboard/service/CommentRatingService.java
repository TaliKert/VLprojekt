package com.kmk.imageboard.service;

import com.kmk.imageboard.model.Comment;
import com.kmk.imageboard.model.DTO.CommentDTO;
import com.kmk.imageboard.model.Image;
import com.kmk.imageboard.model.Rating;
import com.kmk.imageboard.model.User;
import com.kmk.imageboard.repository.CommentRepository;
import com.kmk.imageboard.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentRatingService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    RatingRepository ratingRepository;

    public long getRating(Image image) {
        return ratingRepository.countByImageAndRatingValueIsTrue(image) - ratingRepository.countByImageAndRatingValueIsFalse(image);
    }

    public Boolean getUserRating(User user, Image image) {
        Rating ratingByUserAndImage = ratingRepository.findRatingByUserAndImage(user, image);
        if (ratingByUserAndImage == null) {
            return null;
        }
        return ratingByUserAndImage.isRatingValue();
    }

    public List<CommentDTO> getCommentDTOs(Image image) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByImage(image);
        for (Comment comment : comments) {
            commentDTOs.add(new CommentDTO(comment.getUser().getUsername(), comment.getCommentValue()));
        }
        return commentDTOs;
    }

    public void rateImage(User user, Image image, boolean rating) {
        Rating ratingPOJO = new Rating(user, image, rating);
        Rating currentRating = ratingRepository.findRatingByUserAndImage(user, image);
        if (currentRating == null) {
            ratingRepository.save(ratingPOJO);
        } else if (!(currentRating.isRatingValue() == rating)) {
            ratingRepository.delete(currentRating);
            ratingRepository.save(ratingPOJO);
        }
    }

    public void commentImage(User user, Image image, String comment) {
        commentRepository.save(new Comment(user, image, comment));
    }
}
