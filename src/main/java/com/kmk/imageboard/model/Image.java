package com.kmk.imageboard.model;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uploader_id")
    private long uploaderId;

    @Column(name = "file_ext")
    private String fileExtension;

    public Image() {
    }

    public Image(long uploaderId, String fileExtension) {
        this.uploaderId = uploaderId;
        this.fileExtension = fileExtension;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(long uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
