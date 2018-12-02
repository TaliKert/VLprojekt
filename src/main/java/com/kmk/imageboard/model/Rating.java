package com.kmk.imageboard.model;

import javax.persistence.*;

@Entity
@Table(
        name = "ratings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "image_id"})
        })
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(name = "rating_value")
    private Boolean ratingValue;

    public Rating() {
    }

    public Rating(User user, Image image, Boolean ratingValue) {
        this.user = user;
        this.image = image;
        this.ratingValue = ratingValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Boolean isRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(boolean ratingValue) {
        this.ratingValue = ratingValue;
    }
}
