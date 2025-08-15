package com.sadiqov.sport_place_app.mapper;

import com.sadiqov.sport_place_app.entity.Category;
import com.sadiqov.sport_place_app.dto.PlaceDtos.*;


public class CategoryMapper {

    public static Category toEntity(CreateCategoryRequest request) {
        return Category.builder()
                .name(request.name())
                .slug(request.slug())
                .icon(request.icon())
                .build();
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getIcon()
        );
    }
}
