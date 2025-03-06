package com.example.SocialMedia.controllers;

import com.example.SocialMedia.dtos.LoginRequestDTO;
import com.example.SocialMedia.dtos.UserDTO;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, UserDTO> redisTemplate;

    private static final String USER_KEY_PREFIX = "USER_";

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/{uId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long uId) throws AccessDeniedException {
        return ResponseEntity.ok(userService.getUserById(uId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
         userService.createUser(user);
        UserDTO userDTO = userService.convertToDTO(user);
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), userDTO, 10, TimeUnit.MINUTES);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser( @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser( userDTO);
//        redisTemplate.opsForValue().set(USER_KEY_PREFIX + id, updatedUser, 10, TimeUnit.MINUTES);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/login")
    public  ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        System.out.println("controlllller");
        return ResponseEntity.ok(userService.verify(loginRequestDTO));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        userService.deleteUser();
//        redisTemplate.delete(USER_KEY_PREFIX + id); // Remove from cache
        return ResponseEntity.ok("User deleted successfully");
    }
}
