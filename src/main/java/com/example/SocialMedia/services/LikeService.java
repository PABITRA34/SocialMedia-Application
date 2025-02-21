package com.example.SocialMedia.services;

import com.example.SocialMedia.dtos.LikeDTO;
import com.example.SocialMedia.entities.Like;
import com.example.SocialMedia.entities.Post;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.exceptions.LikeNotFoundException;
import com.example.SocialMedia.exceptions.PostNotFoundException;
import com.example.SocialMedia.exceptions.UserNotFoundException;
import com.example.SocialMedia.repository.LikeRepository;
import com.example.SocialMedia.repository.PostRepository;
import com.example.SocialMedia.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void likePost(Long userId, Long postId){
         User user = userRepository.findById(userId).
                 orElseThrow(()-> new UserNotFoundException("user not found"));
         Post post = postRepository.findById(postId).
                 orElseThrow(()-> new PostNotFoundException("Post not found"));

         if(likeRepository.findByUserAndPost(user, post).isPresent()){
             throw new IllegalStateException("Already liked the post");
         }

//         modelMapper.map(LikeDTO, Like.class);
         Like like  = new Like();
         like.setPost(post);
         like.setUser(user);
         likeRepository.save(like);
    }

    public void unlikePost(Long userId, Long postId){
        User user = userRepository.findById(userId).
                orElseThrow(()-> new UserNotFoundException("user not found"));
        Post post = postRepository.findById(postId).
                orElseThrow(()-> new PostNotFoundException("Post not found"));

        Like like  = likeRepository.findByUserAndPost(user, post).
                orElseThrow(()-> new LikeNotFoundException("Like not found"));

        likeRepository.delete(like);
    }

    public List<LikeDTO> getLikesByPost(Long postId){
        Post post = postRepository.findById(postId).
                orElseThrow(()-> new PostNotFoundException("post not found"));
        List<Like> likes= likeRepository.findByPost(post);
       return likes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private LikeDTO convertToDTO(Like like) {
        return new LikeDTO(like.getId(),like.getUser().getId(),like.getPost().getId());
    }

}
