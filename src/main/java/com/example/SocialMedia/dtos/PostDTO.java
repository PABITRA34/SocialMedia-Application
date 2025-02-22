package com.example.SocialMedia.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
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
//    private LocalDateTime createdAt;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdAt;
}
