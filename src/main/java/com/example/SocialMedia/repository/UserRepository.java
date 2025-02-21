package com.example.SocialMedia.repository;

import com.example.SocialMedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUserName(String username);
//    Optional<User> findByEmail(String email);
    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String username);
}

