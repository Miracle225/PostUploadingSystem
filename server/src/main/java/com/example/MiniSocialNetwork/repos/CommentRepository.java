package com.example.MiniSocialNetwork.repos;

import com.example.MiniSocialNetwork.entity.Comment;
import com.example.MiniSocialNetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByPost(Post post);
    Comment findByIdAndUserId(Long id,Long userId);
}
