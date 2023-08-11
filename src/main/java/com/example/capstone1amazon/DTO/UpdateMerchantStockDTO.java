package com.example.capstone1amazon.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateMerchantStockDTO {

    @NotNull(message = "the amount field is required.")
    @Positive(message = "the amount field must be positive.")
    @JsonValue
    private Integer amount;

    // reason on why I decided to skip lombok AllArgsConstructor for this DTO.
    // https://github.com/FasterXML/jackson-databind/issues/3085
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UpdateMerchantStockDTO(Integer amount) {
        this.amount = amount;
    }
}
