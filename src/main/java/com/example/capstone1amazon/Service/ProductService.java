package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.DTO.UpdateProductDTO;
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

    public void saveProduct(Product product) {
        products.put(product.getId(), product);
    }

    public Product updateProduct(Integer id, UpdateProductDTO updateProductDTO) {
        Product saved_product = products.get(id);

        saved_product.setName(updateProductDTO.getName());
        saved_product.setPrice(updateProductDTO.getPrice());
        saved_product.setCategoryId(updateProductDTO.getCategoryId());

        return saved_product;
    }

    public Product deleteProduct(Integer id) {
        Product saved_product = products.get(id);

        products.remove(id);

        return saved_product;
    }

    public Product getProductById(Integer id) {
        return products.get(id);
    }
}
