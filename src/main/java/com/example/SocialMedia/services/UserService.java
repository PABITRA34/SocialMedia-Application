package com.example.SocialMedia.services;

import com.example.SocialMedia.dtos.LoginRequestDTO;
import com.example.SocialMedia.dtos.UserDTO;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.exceptions.DuplicateResourceException;
import com.example.SocialMedia.exceptions.UserNotFoundException;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.security.JWTService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserService  {

    private static final String USER_CACHE_PREFIX = "user:"; // Prefix for Redis keys

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;



    @Autowired
    private RedisTemplate<String, UserDTO> redisTemplate; // Inject Redis

    @Transactional
    public synchronized UserDTO createUser(User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new DuplicateResourceException("Username already exists");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public String verify(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUserName(loginRequestDTO.getUserName());
        System.out.println("entered");
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName(), loginRequestDTO.getPassword()));
        System.out.println("middle");
        if(authentication.isAuthenticated()){
            System.out.println("auth success");
            return jwtService.generateToken(user.getUserName());
        }
        System.out.println("exited");
        return "Wrong UserName or Password";
    }


    public UserDTO getUserById(@PathVariable Long uId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(uId);
        // Check if the user exists
        if (user.isPresent()) {
            return convertToDTO(user.get());
        } else {
            throw new UserNotFoundException("User not found with id " + uId);
        }
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO updateUser( UserDTO updatedUserDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName);
        System.out.println("entered");
        Optional.ofNullable(updatedUserDTO.getUserName()).ifPresent(user::setUserName);
        Optional.ofNullable(updatedUserDTO.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(updatedUserDTO.getProfilePicture()).ifPresent(user::setProfilePicture);
        Optional.ofNullable(updatedUserDTO.getBio()).ifPresent(user::setBio);

        User updatedUser = userRepository.save(user);
        // Update cache
        saveUserInCache(updatedUser);
        System.out.println("exited");
        return convertToDTO(updatedUser);
    }

    public void deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user = userRepository.findByUserName(userName);
        Long uId = user.getId();
        userRepository.delete(user);
        // Remove user from cache
        redisTemplate.delete(USER_CACHE_PREFIX + uId);
    }

    private void saveUserInCache(User user) {
        String cacheKey = USER_CACHE_PREFIX + user.getId();
        UserDTO userDTO = convertToDTO(user);
        redisTemplate.opsForValue().set(cacheKey, userDTO, 10, TimeUnit.MINUTES);
    }

    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(),
                user.getProfilePicture(), user.getBio());
    }
}
