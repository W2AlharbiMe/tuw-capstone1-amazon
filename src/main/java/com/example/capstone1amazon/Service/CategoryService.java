package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.DTO.UpdateCategoryDTO;
import com.example.capstone1amazon.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CategoryService {
    private final HashMap<Integer, Category> categories = new HashMap<>();


    public final Collection<Category> getCategories() {
        return categories.values();
    }

    public boolean containsId(Integer id) {
        return categories.containsKey(id);
    }

    public void saveCategory(Category category) {
        categories.put(category.getId(), category);
    }

    public Category updateCategory(Integer id, UpdateCategoryDTO updateCategoryDTO) {
        Category saved_category = categories.get(id);

        saved_category.setName(
            updateCategoryDTO.getName()
        );

        return saved_category;
    }
}
