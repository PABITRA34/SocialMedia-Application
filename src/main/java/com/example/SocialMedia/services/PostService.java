package com.example.SocialMedia.services;
import com.example.SocialMedia.dtos.PostDTO;
import com.example.SocialMedia.entities.Post;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.exceptions.UserNotFoundException;
import com.example.SocialMedia.repository.PostRepository;
import com.example.SocialMedia.repository.UserRepository;
import org.hibernate.Cache;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final String POST_CACHE_PREFIX="post:";
    private static final String USER_POSTS_CACHE_PREFIX = "user_posts:";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RedisTemplate<String, PostDTO> redisTemplate;

    @Transactional
    public synchronized PostDTO createPost(PostDTO postDTO) {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName());

        // Map DTO to entity and set user
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user);  // Associate user with post

        // Save post and cache it
        Post savedPost = postRepository.save(post);
        savePostInCache(savedPost);

        return convertToDTO(savedPost);
    }

    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public PostDTO getPostsByUserId(Long userId) {
        String CacheKey = USER_POSTS_CACHE_PREFIX + userId;

        // Check Redis Cache
        PostDTO cachedPosts =  (PostDTO) redisTemplate.opsForValue().get(CacheKey);
        System.out.println(cachedPosts);

        if(cachedPosts != null){
            System.out.println("Returned user posts from cache");
            return cachedPosts;
        }
        // Fetch from DB and store in cache
        List<Post> posts = postRepository.findByUserId(userId);
//        List<PostDTO> postDTOs =  posts.stream().map(this::convertToDTO).collect(Collectors.toList());
          PostDTO postDTO = convertToDTO(posts.get(0));
        System.out.println("Fetched user posts from database");
        redisTemplate.opsForValue().set(CacheKey, postDTO, 10, TimeUnit.MINUTES );
//        posts.stream().map(this::savePostInCache).collect(Collectors.toList());
        return postDTO;
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    private PostDTO savePostInCache(Post post ){
        String cacheKey = POST_CACHE_PREFIX + post.getId();
        PostDTO postDTO = convertToDTO(post);
        redisTemplate.opsForValue().set(cacheKey,postDTO, 10, TimeUnit.MINUTES);
        return postDTO;
    }

    private PostDTO convertToDTO(Post post) {
        return new PostDTO(post.getId(),post.getContent(), post.getImageUrl());
    }

    public PostDTO editPost(Long pId, PostDTO postDTO) {
        // Fetch the existing post from the database
        Post post = postRepository.findById(pId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName());

        // Ensure the authenticated user is the owner of the post
        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to edit this post.");
        }

        // Update post fields
        if (postDTO.getContent() != null) {
            post.setContent(postDTO.getContent());
        }
        if (postDTO.getImageUrl() != null) {
            post.setImageUrl(postDTO.getImageUrl());
        }
        // Save the updated post
        Post updatedPost = postRepository.save(post);
        savePostInCache(updatedPost);
        return convertToDTO(updatedPost);
    }

}
