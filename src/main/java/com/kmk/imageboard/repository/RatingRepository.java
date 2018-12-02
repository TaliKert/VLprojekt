package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.Image;
import com.kmk.imageboard.model.Rating;
import com.kmk.imageboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Long countByImageAndRatingValueIsFalse(Image image);

    Long countByImageAndRatingValueIsTrue(Image image);

    Boolean findRatingByUser(User user);

    Rating findRatingByUserAndImage(User user, Image image);

}
