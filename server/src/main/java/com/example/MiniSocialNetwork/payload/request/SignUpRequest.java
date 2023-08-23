package com.example.MiniSocialNetwork.payload.request;

import com.example.MiniSocialNetwork.annotations.PasswordMatches;
import com.example.MiniSocialNetwork.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
@PasswordMatches
public class SignUpRequest {
    @Email(message = "It must be an email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "Enter your first name")
    private String firstName;
    @NotEmpty(message = "Enter your last name")
    private String lastName;
    @NotEmpty(message = "Enter your username")
    private String username;
    @NotEmpty(message = "Enter your password")
    @Size(min = 8)
    private String password;
    private String confirmPassword;

}
