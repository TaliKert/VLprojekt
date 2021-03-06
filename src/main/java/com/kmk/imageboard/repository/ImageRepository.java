package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM images WHERE id = :thumbID")
    Image getThumbnail(@Param("thumbID") Integer id);

    Image findFirstByIdLessThanOrderByIdDesc(long id);

    Image findFirstByIdGreaterThanOrderByIdDesc(long id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM images ORDER BY id DESC LIMIT 1")
    Image getFirstThumb();

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM images INNER JOIN users ON users.id = images.uploader_id WHERE username = :uname")
    Integer getUploaderUploadCount(@Param("uname") String username);

    Image findImageById(long id);

}
