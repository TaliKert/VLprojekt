package com.kmk.imageboard.model.DTO;

public class ImageInfoDTO {

    private final Long id;
    private final String fileName;

    public ImageInfoDTO(Long id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }
}
