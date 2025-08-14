package com.sadiqov.sport_place_app.controller;

import com.sadiqov.sport_place_app.dto.PlaceDtos;
import com.sadiqov.sport_place_app.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService service;

    @PostMapping("/create")
    public PlaceDtos.PlaceResponse create(@RequestBody @Valid PlaceDtos.CreatePlaceRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public PlaceDtos.PlaceResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public PlaceDtos.PlaceResponse update(@PathVariable Long id, @RequestBody @Valid PlaceDtos.UpdatePlaceRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public Page<PlaceDtos.PlaceResponse> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean openNow,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) BigDecimal lat,
            @RequestParam(required = false) BigDecimal lng,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.search(q, categoryId, openNow, minScore, lat, lng, sortBy, page, size);
    }
}
