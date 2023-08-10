package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.ApiResponse.SimpleApiResponse;
import com.example.capstone1amazon.Model.Category;
import com.example.capstone1amazon.Service.CategoryService;
import com.example.capstone1amazon.Service.ErrorsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;
    private final ErrorsService errorsService;


    @GetMapping("/get")
    public ResponseEntity<ArrayList<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }


    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@RequestBody @Valid Category category, Errors errors) {
        if(categoryService.containsId(category.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("category", "the id must be unique.", "id", "unique")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsService.bulkAdd(errors).get());
        }

        categoryService.saveCategory(category);
        return ResponseEntity.ok((new ApiResponseWithData<Category>("The category have been created.", category)));
    }


}

