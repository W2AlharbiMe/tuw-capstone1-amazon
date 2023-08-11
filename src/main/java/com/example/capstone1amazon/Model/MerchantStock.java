package com.example.capstone1amazon.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotNull(message = "the id field is required.")
    @Positive(message = "the id field must be positive.")
    private Integer id;

    @NotNull(message = "the product id field is required.")
    @Positive(message = "the product id field must be positive.")
    private Integer productId;

    @NotNull(message = "the merchant id field is required.")
    @Positive(message = "the merchant id field must be positive.")
    private Integer merchantId;

    @NotNull(message = "the stock field is required.")
    @Positive(message = "the stock field must be positive.")
    @Min(value = 11, message = "the stock field have to be more than 10 at start.")
    private Integer stock;
}
