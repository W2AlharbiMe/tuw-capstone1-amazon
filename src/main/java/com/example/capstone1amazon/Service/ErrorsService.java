package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;

@Service
public class ErrorsService {

    private final ArrayList<ApiErrorResponse> errorResponses = new ArrayList<>();

    public ErrorsService bulkAdd(Errors errors) {
        if(errors.hasErrors()) {
            for(FieldError fieldError: errors.getFieldErrors()) {
                add(fieldError.getObjectName(), fieldError.getDefaultMessage(), fieldError.getField(), fieldError.getCode());
            }
        }

        return this;
    }

    public void add(String objectName, String defaultMessage, String field, String code) {
        errorResponses.add((new ApiErrorResponse(objectName, defaultMessage, field, code)));
    }

    public ArrayList<ApiErrorResponse> get() {
        ArrayList<ApiErrorResponse> copy = new ArrayList<>(errorResponses);

        errorResponses.clear();

        return copy;
    }

}

