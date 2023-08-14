package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.DTO.UpdateCategoryDTO;
import com.example.capstone1amazon.Model.Category;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final HashMap<Integer, Category> categories = new HashMap<>();

    @Getter
    private final ErrorsService errorsService;


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

    public Category deleteCategory(Integer id) {
        Category saved_category = categories.get(id);

        categories.remove(id);

        return saved_category;
    }

    public Category getCategoryById(Integer id) {
        return categories.get(id);
    }
}
