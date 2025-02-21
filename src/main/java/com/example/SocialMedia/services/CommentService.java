package com.example.SocialMedia.services;

import com.example.SocialMedia.dtos.CommentDTO;
import com.example.SocialMedia.entities.Comment;
import com.example.SocialMedia.entities.Post;
import com.example.SocialMedia.entities.User;
import com.example.SocialMedia.exceptions.CommentNotFound;
import com.example.SocialMedia.exceptions.PostNotFoundException;
import com.example.SocialMedia.exceptions.PostNotFoundException;
import com.example.SocialMedia.exceptions.UserNotFoundException;
import com.example.SocialMedia.repository.CommentRepository;
import com.example.SocialMedia.repository.PostRepository;
import com.example.SocialMedia.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CommentDTO addComment(Long userId, Long postId, CommentDTO commentDTO){
        User user =  userRepository.findById(userId).
                orElseThrow(()->new UserNotFoundException("User not found"));
        Post post = postRepository.findById(postId).
                orElseThrow(()-> new PostNotFoundException("Post doesn't exist"));

        Comment comment = modelMapper.map(commentDTO, Comment.class);

        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);

    }

    public List<CommentDTO> getCommentsByPost(Long postId){
        Post post = postRepository.findById(postId).
                orElseThrow(()-> new PostNotFoundException("Post not found"));
        List<Comment> comments = commentRepository.findByPost(post);
        assert comments != null;
        return comments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CommentDTO updateComment(Long commentId, CommentDTO updatedCommentDTO){
        Comment comment = commentRepository.findById(commentId).
                orElseThrow(()->new CommentNotFound("Comment Not Found"));
        comment.setContent(updatedCommentDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }

    public String  deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new CommentNotFound("Comment Not Found"));
        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }




    private CommentDTO convertToDTO(Comment comment){
        return new CommentDTO(comment.getId(), comment.getUser().getId(), comment.getPost().getId(), comment.getContent(), comment.getCreatedAt());
    }


}
