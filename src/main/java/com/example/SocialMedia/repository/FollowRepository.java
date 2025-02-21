package com.example.SocialMedia.repository;

import com.example.SocialMedia.entities.Follow;
import com.example.SocialMedia.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    /*
    existsByFollowerAndFollowing(...): Checks if a follow relationship already exists.
    findByFollowerAndFollowing(...): Finds a specific follow entry for unfollowing.
     */
    boolean existsByFollowerAndFollowing(User follower, User following);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
