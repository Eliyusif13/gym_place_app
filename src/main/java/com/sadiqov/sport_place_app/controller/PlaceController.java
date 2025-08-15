package com.sadiqov.sport_place_app.controller;

import com.sadiqov.sport_place_app.dto.PlaceDtos.*;
import com.sadiqov.sport_place_app.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody CreatePlaceRequest request) {
         placeService.createPlace(request);
         return new ResponseEntity<> (HttpStatus.OK);
    }

    @GetMapping
    public List<PlaceResponse> getAll() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public PlaceResponse getById(@PathVariable Long id) {
        return placeService.getPlaceById(id);
    }

    @PutMapping("/{id}")
    public PlaceResponse update(@PathVariable Long id, @RequestBody CreatePlaceRequest request) {
        return placeService.updatePlace(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        placeService.deletePlace(id);
    }
}