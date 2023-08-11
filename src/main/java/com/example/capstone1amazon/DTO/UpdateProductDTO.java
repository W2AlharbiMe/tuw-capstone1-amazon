package com.example.capstone1amazon.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateProductDTO {

    @NotEmpty(message = "the name field is required.")
    @Size(min = 4, message = "the category name must be more than 3 length long.")
    private String name;


    @NotNull(message = "the price field is required.")
    @Positive(message = "the price field must be positive.")
    private double price;

    @NotNull(message = "the category id field is required.")
    @Positive(message = "the category id field must be positive.")
    private Integer categoryId;
}
