package com.example.MiniSocialNetwork.repos;

import com.example.MiniSocialNetwork.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel,Long> {
    Optional<ImageModel> findByUserId(Long id);
    Optional<ImageModel> findByPostId(Long id);

}
