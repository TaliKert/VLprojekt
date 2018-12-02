package com.kmk.imageboard.model.DTO;

import java.util.List;

public class SocialDTO {

    private boolean isAuthor;

    private String authorName;

    private long rating;

    private Boolean userRating;

    private List<CommentDTO> comments;

    public SocialDTO(boolean isAuthor, String authorName, long rating, Boolean userRating, List<CommentDTO> comments) {
        this.isAuthor = isAuthor;
        this.authorName = authorName;
        this.rating = rating;
        this.userRating = userRating;
        this.comments = comments;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Boolean getUserRating() {
        return userRating;
    }

    public long getRating() {
        return rating;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }
}
