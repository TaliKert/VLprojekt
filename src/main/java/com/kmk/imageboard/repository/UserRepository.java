package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /* Läbi selle interface'i on võimalik andmebaasiga suhelda */

}
