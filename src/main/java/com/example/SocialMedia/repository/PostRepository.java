package com.example.SocialMedia.repository;


import com.example.SocialMedia.entities.Post;
import com.example.SocialMedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    /*
      JPA automatically provides implementation in this case
      List<Post> findByUserId(Long userId);// Get all posts by a specific user
      But if we want
      More Control over the Query using JPQL
     */

     @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findByUserId(@Param("userId") Long userId);// Get all posts by a specific user
}
