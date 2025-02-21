package com.example.SocialMedia.services;

import com.example.SocialMedia.entities.Follow;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.exceptions.UserNotFoundException;
import com.example.SocialMedia.repository.FollowRepository;
import com.example.SocialMedia.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Transactional
    public String followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("User to follow not found"));

        // Prevent self-following
        if (follower.equals(following)) {
            return "You cannot follow yourself!";
        }

        // Check if already following
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            return "You are already following this user!";
        }

        // Save follow entry
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);

        return "You are now following " + following.getUserName();
    }

    @Transactional
    public String unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("User to unfollow not found"));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new IllegalStateException("You are not following this user!"));

        followRepository.delete(follow);

        return "You have unfollowed " + following.getUserName();
    }

}
