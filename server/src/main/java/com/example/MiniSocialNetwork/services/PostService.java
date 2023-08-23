package com.example.MiniSocialNetwork.services;

import com.example.MiniSocialNetwork.dto.PostDTO;
import com.example.MiniSocialNetwork.entity.ImageModel;
import com.example.MiniSocialNetwork.entity.Post;
import com.example.MiniSocialNetwork.entity.User;
import com.example.MiniSocialNetwork.exceptions.PostNotFoundException;
import com.example.MiniSocialNetwork.repos.ImageRepository;
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
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setLikes(0);
        post.setLikedUsers(postDTO.getUserLiked());
        post.setUser(user);

        LOG.info("Saving post for User {}:" + user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository
                .findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cant be found for username:" + user.getUsername()));
    }

    public List<Post> getAllPostsByUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    public Post likePost(Long postId, String username) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        Optional<String> userLikes = post.getLikedUsers()
                .stream()
                .filter(u -> u.equals(username))
                .findAny();

        if (userLikes.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }

    public void deletePost(Long postId,Principal principal){
        Post post = getPostById(postId,principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }
}
