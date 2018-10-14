package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
