package com.example.MiniSocialNetwork.repos;

import com.example.MiniSocialNetwork.entity.Post;
import com.example.MiniSocialNetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByUserOrderByCreatedAtDesc(User user);
    List<Post> findAllByOrderByCreatedAtDesc();
    Optional<Post> findPostByIdAndUser(Long id,User user);
}
