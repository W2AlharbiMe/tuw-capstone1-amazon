package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.Model.Product;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;

@Service
public class ProductService {
    private final HashMap<Integer, Product> products = new HashMap<>();


    public Collection<Product> getAllProducts() {
        return products.values();
    }


    public boolean containsId(Integer id) {
        return products.containsKey(id);
    }
}
