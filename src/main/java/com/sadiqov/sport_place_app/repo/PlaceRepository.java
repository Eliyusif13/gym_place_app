package com.sadiqov.sport_place_app.repo;

import com.sadiqov.sport_place_app.entity.Category;
import com.sadiqov.sport_place_app.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByCategory(Category category);
    boolean existsByNameIgnoreCase(String name);
}