package com.sadiqov.sport_place_app.service;

import com.sadiqov.sport_place_app.entity.Category;
import com.sadiqov.sport_place_app.repo.CategoryRepository;
import com.sadiqov.sport_place_app.mapper.CategoryMapper;
import com.sadiqov.sport_place_app.dto.PlaceDtos.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    @Transactional
    public void createCategory(CreateCategoryRequest request) {
        if (categoryRepo.existsBySlug(request.slug())) {
            throw new IllegalArgumentException("Slug already exists");
        }
        Category category = CategoryMapper.toEntity(request);
        categoryRepo.save(category);
        CategoryMapper.toResponse(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return CategoryMapper.toResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CreateCategoryRequest request) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setName(request.name());
        category.setSlug(request.slug());
        category.setIcon(request.icon());
        categoryRepo.save(category);
        return CategoryMapper.toResponse(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        categoryRepo.delete(category);
    }
}
