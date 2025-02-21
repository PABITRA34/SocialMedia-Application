package com.example.SocialMedia.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follows", uniqueConstraints = {
                  @UniqueConstraint(columnNames = {"follower_id", "following_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;  // The user who is following

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;  // The user being followed
}
