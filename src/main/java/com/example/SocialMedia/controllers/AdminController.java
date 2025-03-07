package com.example.SocialMedia.controllers;

import com.example.SocialMedia.dtos.UserDTO;
import com.example.SocialMedia.entities.Role;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.services.RoleService;
import com.example.SocialMedia.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")  // This is the base URL for all admin routes
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser() {
        userService.deleteUser();
    }

    @PutMapping("/user/{id}")
    public UserDTO updateUser( @RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @PostMapping("/user")
    public UserDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/create-admin")
    public UserDTO createAdmin(@RequestBody User user) {
        // Create the new user with the 'ADMIN' role
        Role adminRole = roleService.findByName("ADMIN");
        user.getRoles().add(adminRole);  // Assign the 'ADMIN' role to the user
        // Create the user
        return userService.createUser(user);
    }  
}

