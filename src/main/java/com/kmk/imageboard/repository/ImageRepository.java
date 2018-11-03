package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM images WHERE id = (SELECT MAX(id) FROM images WHERE id < :thumbID)")
    Image getNextThumb(@Param("thumbID") Integer id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM images ORDER BY id DESC LIMIT 1")
    Image getFirstThumb();
}
