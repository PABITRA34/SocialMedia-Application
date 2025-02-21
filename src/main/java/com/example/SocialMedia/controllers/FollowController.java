package com.example.SocialMedia.controllers;

import com.example.SocialMedia.repository.FollowRepository;
import com.example.SocialMedia.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{followerId}/{followingId}")
    public String followUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        return followService.followUser(followerId, followingId);
    }

    @DeleteMapping("/{followerId}/{followingId}")
    public String unfollowUser(@PathVariable Long followerId, @PathVariable Long followingId) {
        return followService.unfollowUser(followerId, followingId);
    }

}
