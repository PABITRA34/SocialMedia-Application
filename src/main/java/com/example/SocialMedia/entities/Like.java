package com.example.SocialMedia.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="likes" , uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id","post_id"}) //prevents duplicate likes
})
@Getter
@Setter
@NoArgsConstructor

public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user ;// User who liked the post

    @ManyToOne
    @JoinColumn(name = "post_id", nullable=false)
    private Post post;// Post being liked
}
