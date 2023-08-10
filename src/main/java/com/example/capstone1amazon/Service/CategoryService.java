package com.example.capstone1amazon.Service;

import com.example.capstone1amazon.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CategoryService {
    private final ArrayList<Category> categories = new ArrayList<>();
    private final HashMap<Integer, Boolean> unique_index_map = new HashMap<>();

    public final ArrayList<Category> getCategories() {
        return categories;
    }

    public boolean isDuplicateId(Integer id) {
        return unique_index_map.containsKey(id);
    }

    public void saveCategory(Category category) {
        unique_index_map.put(category.getId(), true);
        categories.add(category);
    }
}
