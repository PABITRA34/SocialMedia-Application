package com.example.SocialMedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime createdAt;
}
