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
        unique_id_map.put(category.getId(), (categories.size() - 1));
        categories.add(category);
    }
}
