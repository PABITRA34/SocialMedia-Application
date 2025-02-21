package com.example.SocialMedia.controllers;

import com.example.SocialMedia.dtos.CommentDTO;
import com.example.SocialMedia.entities.Comment;
import com.example.SocialMedia.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add/{userId}/{postId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long userId, @PathVariable Long postId, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.addComment(userId, postId, commentDTO));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId){
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<CommentDTO> updateCommentById(@PathVariable Long commentId, @RequestBody CommentDTO updatedCommentDTO){
        return ResponseEntity.ok(commentService.updateComment(commentId, updatedCommentDTO));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId){
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }

}
