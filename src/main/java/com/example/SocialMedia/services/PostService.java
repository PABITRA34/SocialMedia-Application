package com.example.SocialMedia.services;
import com.example.SocialMedia.dtos.PostDTO;
import com.example.SocialMedia.entities.Post;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.exceptions.UserNotFoundException;
import com.example.SocialMedia.repository.PostRepository;
import com.example.SocialMedia.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PostDTO createPost(PostDTO postDTO) {
        // Validate if user exists
        User user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = modelMapper.map(postDTO, Post.class);

        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    private PostDTO convertToDTO(Post post) {
        return new PostDTO(post.getId(), post.getUser().getId(), post.getContent(), post.getImageUrl(), post.getCreatedAt());
    }

}
