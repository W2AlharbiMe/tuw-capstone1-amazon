package com.example.capstone1amazon.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema()
public class Cart {
    @NotNull(message = "the id field is required.")
    @Positive(message = "the id field must be positive.")
    private Integer id;


    @NotNull(message = "the user id field is required.")
    @Positive(message = "the user id field must be positive.")
    private Integer userId;


    @NotNull(message = "the product id field is required.")
    @Positive(message = "the product  id field must be positive.")
    private Integer productId;


    @NotNull(message = "the amount field is required.")
    @Positive(message = "the amount field must be positive.")
    private Integer amount;
}
