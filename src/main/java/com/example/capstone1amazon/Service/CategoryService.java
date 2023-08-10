package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.DTO.UpdateCategoryDTO;
import com.example.capstone1amazon.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CategoryService {
    private final ArrayList<Category> categories = new ArrayList<>();
    private final HashMap<Integer, Integer> unique_id_map = new HashMap<>();

    public final ArrayList<Category> getCategories() {
        return categories;
    }

    public boolean containsId(Integer id) {
        return unique_id_map.containsKey(id);
    }

    public void saveCategory(Category category) {
        categories.add(category);
        unique_id_map.put(category.getId(), (categories.size() - 1));
    }

    public Category updateCategory(Integer id, UpdateCategoryDTO updateCategoryDTO) {
        Category saved_category = categories.get(
                unique_id_map.get(id)
        );

        // only update name. no need to update the id.
        // if the id changed then I need to recreate the unique_id_map.
        saved_category.setName(
            updateCategoryDTO.getName()
        );

        return saved_category;
    }
}
