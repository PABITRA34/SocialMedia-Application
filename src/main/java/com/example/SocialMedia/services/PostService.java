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
        // Validate if user exists
        User user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = modelMapper.map(postDTO, Post.class);

        Post savedPost = postRepository.save(post);
        //cache the post
//        savePostInCache(savedPost);
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
        return new PostDTO(post.getId(), post.getUser().getId(), post.getContent(), post.getImageUrl());
    }

}
