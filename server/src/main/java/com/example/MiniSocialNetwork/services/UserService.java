package com.example.MiniSocialNetwork.services;

import com.example.MiniSocialNetwork.dto.UserDTO;
import com.example.MiniSocialNetwork.entity.User;
import com.example.MiniSocialNetwork.entity.enums.ERole;
import com.example.MiniSocialNetwork.exceptions.UserExistsException;
import com.example.MiniSocialNetwork.payload.request.SignUpRequest;
import com.example.MiniSocialNetwork.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User saveUser(SignUpRequest request) {
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setUsername(request.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            user.getRole().add(ERole.ROLE_USER);
            LOG.info("Saving user {}," + request.getEmail());
            return userRepository.save(user);
        } catch (Exception ex) {
            LOG.error("Error during registration" + ex.getMessage());
            throw new UserExistsException("User " + request.getUsername() + " already exists.");
        }
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
