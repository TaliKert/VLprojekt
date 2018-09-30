package com.kmk.imageboard.repository;

import com.kmk.imageboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UserRepository extends JpaRepository<User, Long> {

    /* Läbi selle interface'i on võimalik andmebaasiga suhelda */
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO users (username, email, registration_date, google_id) " +
                                        "VALUES (:username, :email, :registrationDate, :googleId)")
    void save(@Param("username") String username,
              @Param("email") String email,
              @Param("registrationDate") LocalDate registrationDate,
              @Param("googleId") String googleId);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE google_id = :googleId")
    User findByGoogleId(@Param("googleId") String googleId);

    User findByUsername(String username);
}
