package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.ApiResponse.SimpleApiResponse;
import com.example.capstone1amazon.DTO.UpdateCategoryDTO;
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
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;
    private final ErrorsService errorsService;


    @GetMapping("/get")
    public ResponseEntity<Collection<Category>> getCategories() {
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

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody @Valid UpdateCategoryDTO updateCategoryDTO, Errors errors) {
        if(!categoryService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("category", "category not found.", "id", "not_found")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsService.bulkAdd(errors).get());
        }

        Category category = categoryService.updateCategory(id, updateCategoryDTO);
        return ResponseEntity.ok((new ApiResponseWithData<Category>("The category have been updated.", category)));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        if(!categoryService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("category", "category not found.", "id", "not_found")));
        }


        Category category = categoryService.deleteCategory(id);

        return ResponseEntity.ok((new ApiResponseWithData<Category>("The category have been deleted.", category)));
    }


}

