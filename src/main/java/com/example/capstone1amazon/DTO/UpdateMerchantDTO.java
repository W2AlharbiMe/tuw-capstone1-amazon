package com.example.capstone1amazon.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UpdateMerchantDTO {

    @NotEmpty(message = "the name field is required.")
    @Size(min = 4, message = "the category name must be more than 3 length long.")
    @JsonValue
    private String name;


    // reason on why I decided to skip lombok AllArgsConstructor for this DTO.
    // https://github.com/FasterXML/jackson-databind/issues/3085
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UpdateMerchantDTO(String name) {
        this.name = name;
    }
}
