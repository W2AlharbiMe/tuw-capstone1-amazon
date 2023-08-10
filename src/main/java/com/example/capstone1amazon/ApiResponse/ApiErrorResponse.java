package com.example.capstone1amazon.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private String objectName;
    private String defaultMessage;
    private String field;
    private String code;
}
