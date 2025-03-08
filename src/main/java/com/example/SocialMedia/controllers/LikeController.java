package com.example.SocialMedia.controllers;

import com.example.SocialMedia.dtos.LikeDTO;
import com.example.SocialMedia.entities.Like;
import com.example.SocialMedia.entities.Post;
import com.example.SocialMedia.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<String> likePost( @PathVariable Long postId){
        likeService.likePost(postId);
        return ResponseEntity.ok("Post Liked Successfully");
    }

    @DeleteMapping("/post/unlike/{postId}")
    public ResponseEntity<String> unlikePost( @PathVariable Long postId){
        likeService.unlikePost( postId);
        return ResponseEntity.ok("Post Unliked Successfully");
    }

    @GetMapping("/post/getAll-likes/{postId}")
    public ResponseEntity<List<LikeDTO>> getLikesByPost(@PathVariable Long postId){
        List<LikeDTO> likes = likeService.getLikesByPost(postId);
        return ResponseEntity.ok(likes);
    }
}
