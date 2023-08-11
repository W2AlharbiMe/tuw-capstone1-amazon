package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.Model.Product;
import com.example.capstone1amazon.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<Collection<Product>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}

