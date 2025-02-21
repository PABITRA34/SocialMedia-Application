package com.example.SocialMedia.repository;

import com.example.SocialMedia.entities.Like;
import com.example.SocialMedia.entities.Post;
import com.example.SocialMedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post); // Find if a user liked a post
    List<Like> findByPost(Post post); // Get all likes for a post
//    List<Like> findByUser(User user); // Get all likes by a user
}
