package com.example.SocialMedia.repository;

import com.example.SocialMedia.entities.Comment;
import com.example.SocialMedia.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post); //  This is a valid JPA query method
}
