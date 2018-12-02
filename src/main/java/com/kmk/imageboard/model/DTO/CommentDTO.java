package com.kmk.imageboard.model.DTO;

public class CommentDTO {

    private String authorName;

    private String text;

    public CommentDTO(String authorName, String text) {
        this.authorName = authorName;
        this.text = text;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getText() {
        return text;
    }
}
