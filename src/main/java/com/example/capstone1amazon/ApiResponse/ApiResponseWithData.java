package com.example.capstone1amazon.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseWithData<T> {
    private String message;
    private T data;
}
