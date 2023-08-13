package com.example.capstone1amazon.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Schema()
public class BuyProductDTO {

    @NotNull(message = "the user id field is required.")
    @Positive(message = "the user id field must be positive.")
    private Integer userId;

    @NotNull(message = "the merchant id field is required.")
    @Positive(message = "the merchant id field must be positive.")
    private Integer merchantId;


    @NotNull(message = "the product id field is required.")
    @Positive(message = "the product id field must be positive.")
    private Integer productId;


    @NotNull(message = "the amount field is required.")
    @Positive(message = "the amount field must be positive at least 1.")
    private Integer amount;
}
