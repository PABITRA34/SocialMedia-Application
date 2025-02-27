//package com.example.SocialMedia.controllers;
//
//import com.example.SocialMedia.dtos.UserDTO;
//import com.example.SocialMedia.entities.User;
//import com.example.SocialMedia.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
//        return ResponseEntity.ok(userService.getUserById(id));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<UserDTO>> getAllUsers(){
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//    @PostMapping
//    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
//        UserDTO user = userService.createUser(userDTO);
//        return ResponseEntity.ok(user);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO){
//        return ResponseEntity.ok(userService.updateUser(id,userDTO));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable Long id){
//        userService.deleteUser(id);
//        return ResponseEntity.ok("user deleted successfully");
//    }
//}


package com.example.SocialMedia.controllers;

import com.example.SocialMedia.dtos.UserDTO;
import com.example.SocialMedia.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
//        String redisKey = USER_KEY_PREFIX + id;
//
//        // Check if user exists in Redis
//        UserDTO cachedUser = (UserDTO) redisTemplate.opsForValue().get(redisKey);
//
//        if (cachedUser != null) {
//            System.out.println("returning from Redis Cache");
//            return ResponseEntity.ok(cachedUser);
//        }

        // Fetch from DB and cache it
        UserDTO user = userService.getUserById(id);
//        redisTemplate.opsForValue().set(redisKey, user, 10, TimeUnit.MINUTES);
//        System.out.println("fetched from db");
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userService.createUser(userDTO);
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), user, 10, TimeUnit.MINUTES);
        return ResponseEntity.ok(user);
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
//        UserDTO user = userService.loginUser(userDTO);
//        redisTemplate.opsForValue().set(USER_KEY_PREFIX + user.getId(), user, 10, TimeUnit.MINUTES);
//        return ResponseEntity.ok(user);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        redisTemplate.opsForValue().set(USER_KEY_PREFIX + id, updatedUser, 10, TimeUnit.MINUTES);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        redisTemplate.delete(USER_KEY_PREFIX + id); // Remove from cache
        return ResponseEntity.ok("User deleted successfully");
    }
}
