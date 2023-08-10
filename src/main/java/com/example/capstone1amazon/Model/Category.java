package com.example.capstone1amazon.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

    @NotNull(message = "the id field is required.")
    @Positive(message = "the id field must be positive.")
    private Integer id;

    @NotEmpty(message = "the name field is required.")
    @Size(min = 3, message = "the category name must be more than 3 length long.")
    private String name;
}
