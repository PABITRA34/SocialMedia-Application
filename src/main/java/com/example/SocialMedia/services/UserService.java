////package com.example.SocialMedia.services;
////
////import com.example.SocialMedia.dtos.UserDTO;
////import com.example.SocialMedia.entities.User;
////import com.example.SocialMedia.exceptions.DuplicateResourceException;
////import com.example.SocialMedia.exceptions.UserNotFoundException;
////import com.example.SocialMedia.repository.UserRepository;
////import jakarta.transaction.Transactional;
////import org.modelmapper.ModelMapper;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////import java.util.List;
////import java.util.Optional;
////import java.util.stream.Collectors;
////
////@Service
////public class UserService {
////
////    @Autowired
////    private UserRepository userRepository;
////
////    @Autowired
////    private ModelMapper modelMapper;
////
////    @Transactional
////    public synchronized UserDTO createUser(UserDTO userDTO) {
////        if (userRepository.existsByUserName(userDTO.getUserName())) {
//////            System.out.println("error while creating");
////            throw new DuplicateResourceException("Username already existss");
////        }
////
////        User user = modelMapper.map(userDTO, User.class);
//////        user.setPassword("hashed password"); // Set password
////
////        User savedUser = userRepository.save(user);
////        return convertToDTO(savedUser);
////    }
////
////
////
////    public UserDTO getUserById(Long userId){
////        User user = userRepository.findById(userId)
////                .orElseThrow(()-> new UserNotFoundException("User not found"));
////        return convertToDTO(user);
////    }
////
////    public List<UserDTO> getAllUsers(){
////        List<User> users = userRepository.findAll();
////        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
////    }
////
////
////    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
////            User user = userRepository.findById(id)
////                    .orElseThrow(()-> new UserNotFoundException("user not found"));
////
////            Optional.ofNullable(updatedUserDTO.getUserName()).ifPresent(user::setUserName);
////            Optional.ofNullable(updatedUserDTO.getEmail()).ifPresent(user::setEmail);
////            Optional.ofNullable(updatedUserDTO.getProfilePicture()).ifPresent(user::setProfilePicture);
////            Optional.ofNullable(updatedUserDTO.getBio()).ifPresent(user::setBio);
////
////            User updatedUser = userRepository.save(user);
////            return convertToDTO(updatedUser);
////    }
////
////
////
////    public void deleteUser(Long id) {
////         User user = userRepository.findById(id)
////                 .orElseThrow(()-> new UserNotFoundException("user not found"));
////
////         userRepository.delete(user);
////
////    }
////
////    public UserDTO convertToDTO(User user){
////        UserDTO userDTO = new UserDTO();
////        userDTO.setId(user.getId());
////        userDTO.setUserName(user.getUserName());
////        userDTO.setEmail(user.getEmail());
////        userDTO.setProfilePicture(user.getProfilePicture());
////        userDTO.setBio(user.getBio());
//////        UserDTO userDTO = this.modelMapper.map(user, UserDTO.class);
////        return userDTO;
////    }
////
////    public User convertToEntity(UserDTO userDTO){
////        User user = this.modelMapper.map(userDTO, User.class);
////        return user;
////    }
////}
//
//
//package com.example.SocialMedia.services;
//
//import com.example.SocialMedia.dtos.UserDTO;
//import com.example.SocialMedia.entities.User;
//import com.example.SocialMedia.exceptions.DuplicateResourceException;
//import com.example.SocialMedia.exceptions.UserNotFoundException;
//import com.example.SocialMedia.repository.UserRepository;
//import jakarta.transaction.Transactional;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class UserService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;  // Password encryption
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getUserName(),
//                user.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//    }
//
//    @Transactional
//    public synchronized UserDTO createUser(UserDTO userDTO) {
//        if (userRepository.existsByUserName(userDTO.getUserName())) {
//            throw new DuplicateResourceException("Username already exists");
//        }
//
//        User user = modelMapper.map(userDTO, User.class);
//        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encrypt password
//
//        User savedUser = userRepository.save(user);
//        return convertToDTO(savedUser);
//    }
//
//    public UserDTO getUserById(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        return convertToDTO(user);
//    }
//
//    public List<UserDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
//    }
//
//    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        Optional.ofNullable(updatedUserDTO.getUserName()).ifPresent(user::setUserName);
//        Optional.ofNullable(updatedUserDTO.getEmail()).ifPresent(user::setEmail);
//        Optional.ofNullable(updatedUserDTO.getProfilePicture()).ifPresent(user::setProfilePicture);
//        Optional.ofNullable(updatedUserDTO.getBio()).ifPresent(user::setBio);
//
//        User updatedUser = userRepository.save(user);
//        return convertToDTO(updatedUser);
//    }
//
//    public void deleteUser(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        userRepository.delete(user);
//    }
//
//    public UserDTO convertToDTO(User user) {
//        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(),
//                user.getProfilePicture(), user.getBio());
//    }
//}



//.......................
package com.example.SocialMedia.services;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;



    @Autowired
    private RedisTemplate<String, UserDTO> redisTemplate; // Inject Redis

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getUserName(),
//                user.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//    }

    @Transactional
    public synchronized UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = modelMapper.map(userDTO, User.class);
//        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
          user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);

        // Cache the user
//        saveUserInCache(savedUser);

        return convertToDTO(savedUser);
    }

    public String verify(UserDTO userDTO) {
        User user = userRepository.findByUserName(userDTO.getUserName());
//
//        // Check if the entered password matches the stored hashed password
//        if (!new BCryptPasswordEncoder().matches(userDTO.getPassword(), user.getPassword())) {
//            throw new UserNotFoundException("Invalid username or password");
//        }
//        // Return user details if login is successful
//        return convertToDTO(user);

        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUserName(), userDTO.getPassword()));

        if(authentication.isAuthenticated()){
            System.out.println("auth success");
            return jwtService.generateToken(user.getUserName());
        }
        return "Wrong UserName or Password";
    }


//    public UserDTO getUserById(Long userId) {
//        String cacheKey = USER_CACHE_PREFIX + userId;
//
//        // Check if user exists in Redis cache
//        UserDTO cachedUser = (UserDTO) redisTemplate.opsForValue().get(cacheKey);
//        if (cachedUser != null) {
//            System.out.println("returned from the cache");
//            return cachedUser; // Return cached user if present
//        }
//
//        // Fetch from DB and cache it
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        UserDTO userDTO = convertToDTO(user);
//        System.out.println("fetched from the db");
//        redisTemplate.opsForValue().set(cacheKey, userDTO, 10, TimeUnit.MINUTES);
//        return userDTO;
//    }


    public ResponseEntity<UserDTO> getUserById(Long id,  String token) throws AccessDeniedException {
        /*String usernameFromToken = jwtService.extractUserName(token.substring(7));  // Remove "Bearer " prefix

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getUserName().equals(usernameFromToken)) {
            System.out.println("Unauthorised");
            throw new AccessDeniedException("You are not authorized to access this resource");
        }

        return ResponseEntity.ok(convertToDTO(user)); */

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User userInDb=  userRepository.findByUserName(userName);
        if(userInDb !=null  && userInDb.getUserName()==userName){
            return new ResponseEntity<>(convertToDTO(userInDb),HttpStatus.OK);
        }
        System.out.println("failed");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO updatedUserDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Optional.ofNullable(updatedUserDTO.getUserName()).ifPresent(user::setUserName);
        Optional.ofNullable(updatedUserDTO.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(updatedUserDTO.getProfilePicture()).ifPresent(user::setProfilePicture);
        Optional.ofNullable(updatedUserDTO.getBio()).ifPresent(user::setBio);

        User updatedUser = userRepository.save(user);

        // Update cache
        saveUserInCache(updatedUser);

        return convertToDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);

        // Remove user from cache
        redisTemplate.delete(USER_CACHE_PREFIX + id);
    }

    private void saveUserInCache(User user) {
        String cacheKey = USER_CACHE_PREFIX + user.getId();
        UserDTO userDTO = convertToDTO(user);
        redisTemplate.opsForValue().set(cacheKey, userDTO, 10, TimeUnit.MINUTES);
    }

    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(),
                user.getProfilePicture(), user.getBio());
    }
}
