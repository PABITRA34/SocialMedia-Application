package com.example.SocialMedia.controllers;

import com.example.SocialMedia.dtos.PostDTO;
import com.example.SocialMedia.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public PostDTO createPost(@RequestBody PostDTO postDTO){
        return postService.createPost(postDTO);
    }

    @GetMapping("/getAllPosts")
    public List<PostDTO> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/user/{userId}")
    public PostDTO getPostById(@PathVariable Long userId){
        return postService.getPostsByUserId(userId);
    }

    @DeleteMapping("/delete/{postId}")
    public String deletePostById(@PathVariable Long postId){
        postService.deletePost(postId);
        return "Post Deleted Successfully";
    }

    @PutMapping("/edit/{pId}")
    public PostDTO editPost(@PathVariable Long pId, @RequestBody PostDTO postDTO){
      return postService.editPost(pId, postDTO);
    }
}
