package com.example.MiniSocialNetwork.controllers;

import com.example.MiniSocialNetwork.entity.ImageModel;
import com.example.MiniSocialNetwork.payload.response.MessageResponse;
import com.example.MiniSocialNetwork.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {

        imageService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageService.uploadImageToPost(file, principal, Long.parseLong(postId));
        return ResponseEntity.ok(new MessageResponse("Image Uploaded Successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageForUser(Principal principal) {
        ImageModel userImage = imageService.getImageToUser(principal);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModel> getImageToPost(@PathVariable("postId") String postId) {
        ImageModel postImage = imageService.getImageToPost(Long.parseLong(postId));
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }
}
