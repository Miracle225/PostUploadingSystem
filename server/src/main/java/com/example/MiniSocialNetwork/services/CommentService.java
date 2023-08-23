package com.example.MiniSocialNetwork.services;

import com.example.MiniSocialNetwork.dto.CommentDTO;
import com.example.MiniSocialNetwork.entity.Comment;
import com.example.MiniSocialNetwork.entity.Post;
import com.example.MiniSocialNetwork.entity.User;
import com.example.MiniSocialNetwork.exceptions.PostNotFoundException;
import com.example.MiniSocialNetwork.repos.CommentRepository;
import com.example.MiniSocialNetwork.repos.PostRepository;
import com.example.MiniSocialNetwork.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId, Principal principal, CommentDTO commentDTO) {
        User user = getUserByPrincipal(principal);
        Post post = getPostById(postId, principal);
        Comment comment = new Comment();
        comment.setMessage(commentDTO.getMessage());
        comment.setUsername(user.getUsername());
        comment.setUserId(user.getId());
        comment.setPost(post);
        LOG.info("Saving comment by User {}:" + user.getEmail() + "on post {}: " + post.getTitle());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cant be found"));
        return commentRepository.findAllByPost(post);

    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository
                .findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cant be found for username:" + user.getUsername()));
    }
}
