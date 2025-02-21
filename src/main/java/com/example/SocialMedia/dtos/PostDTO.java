package com.example.SocialMedia.dtos;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private Long userId;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
}
