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
    private String password;


    @NotEmpty(message = "the email field is required.")
    @Email(message = "invalid email.")
    private String email;

    @NotEmpty(message = "the role field is required.")
//    @Pattern(message = "the role can only be 'Admin' or 'Customer'.", regexp = "")
    private String role;

    @NotNull(message = "the balance field is required.")
    @Positive(message = "the balance field must be positive.")
    private Double balance;
}
