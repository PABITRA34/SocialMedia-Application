package com.example.SocialMedia.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String userName;
    private String email;
    private String password; // Added password field
    private String profilePicture;
    private String bio;
}
