package com.example.SocialMedia.controllers;


import com.example.SocialMedia.services.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/user/{followingId}")
    public String followUser( @PathVariable Long followingId) {
        return followService.followUser(followingId);
    }

    @DeleteMapping("/delete/{followingId}")
    public String unfollowUser( @PathVariable Long followingId) {
        return followService.unfollowUser(followingId);
    }

}
