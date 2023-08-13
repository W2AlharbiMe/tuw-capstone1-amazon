package com.example.capstone1amazon.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema()
public class UpdateUserDTO {
    @NotEmpty(message = "the username field is required.")
    @Size(min = 6, message = "the username must be more than 5 length long.")
    private String username;

    @NotEmpty(message = "the password field is required.")
    @Size(min = 7, message = "the password must be more than 6 length long.")
    @Pattern(message = "the password must contain at least seven characters, at least one number and both lower and uppercase letters and special characters", regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
    private String password;


    @NotEmpty(message = "the email field is required.")
    @Email(message = "invalid email.")
    private String email;

    @NotEmpty(message = "the role field is required.")
    @Pattern(message = "the position can only be either a 'admin' or 'customer'.", regexp = "(?i)\\b(admin)\\b?|(?i)\\b(customer)\\b")
    private String role;

    @NotNull(message = "the balance field is required.")
    @Positive(message = "the balance field must be positive.")
    private Double balance;
}
