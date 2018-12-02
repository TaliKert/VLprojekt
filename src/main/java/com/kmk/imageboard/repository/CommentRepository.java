package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.Comment;
import com.kmk.imageboard.model.Image;
import com.kmk.imageboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUser(User user);

    List<Comment> findAllByImage(Image image);
}
