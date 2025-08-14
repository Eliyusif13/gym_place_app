package com.sadiqov.sport_place_app.controller;

import com.sadiqov.sport_place_app.entity.Category;
import com.sadiqov.sport_place_app.repo.CategoryRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository repo;

    public record CreateCategoryRequest(@NotBlank String name, @NotBlank String slug, String icon) {}
    public record CategoryResponse(Long id, String name, String slug, String icon) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@RequestBody @Valid CreateCategoryRequest req) {
        if (repo.existsBySlug(req.slug())) throw new IllegalArgumentException("Slug already exists");
        Category c = repo.save(Category.builder().name(req.name()).slug(req.slug()).icon(req.icon()).build());
        return new CategoryResponse(c.getId(), c.getName(), c.getSlug(), c.getIcon());
    }

    @GetMapping
    public List<CategoryResponse> all() {
        return repo.findAll().stream().map(c -> new CategoryResponse(c.getId(), c.getName(), c.getSlug(), c.getIcon())).toList();
    }
}
