package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.DTO.UpdateProductDTO;
import com.example.capstone1amazon.Model.Category;
import com.example.capstone1amazon.Model.Product;
import com.example.capstone1amazon.Service.CategoryService;
import com.example.capstone1amazon.Service.ErrorsService;
import com.example.capstone1amazon.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<Collection<Product>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid Product product, Errors errors) {
        if(productService.containsId(product.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("product", "the id must be unique.", "id", "unique")));
        }

        if(!productService.getCategoryService().containsId(product.getCategoryId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("product", "No category have been found with the category id you provided.", "categoryId", "category_not_found")));
        }


        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productService.getErrorsService().bulkAdd(errors).get());
        }

        productService.saveProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body((new ApiResponseWithData<Product>("The product have been created.", product)));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody @Valid UpdateProductDTO updateProductDTO, Errors errors) {
        if(!productService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("product", "product not found.", "id", "not_found")));
        }

        if(!productService.getCategoryService().containsId(updateProductDTO.getCategoryId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("product", "No category have been found with the category id you provided.", "categoryId", "category_not_found")));
        }


        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(productService.getErrorsService().bulkAdd(errors).get());
        }

        Product product = productService.updateProduct(id, updateProductDTO);
        return ResponseEntity.ok((new ApiResponseWithData<Product>("the product have been updated.", product)));
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        if(!productService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("product", "product not found.", "id", "not_found")));
        }

        Product product = productService.deleteProduct(id);

        return ResponseEntity.ok((new ApiResponseWithData<Product>("the product have been deleted.", product)));
    }
}

