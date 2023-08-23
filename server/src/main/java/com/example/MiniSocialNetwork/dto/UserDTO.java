package com.example.MiniSocialNetwork.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class UserDTO {
    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String username;
    private String bio;
}
