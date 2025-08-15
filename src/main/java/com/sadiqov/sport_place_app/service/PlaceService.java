package com.sadiqov.sport_place_app.service;

import com.sadiqov.sport_place_app.dto.PlaceDtos.*;
import com.sadiqov.sport_place_app.entity.Category;
import com.sadiqov.sport_place_app.entity.Place;
import com.sadiqov.sport_place_app.entity.PlaceSchedule;
import com.sadiqov.sport_place_app.mapper.PlaceMapper;
import com.sadiqov.sport_place_app.mapper.PlaceScheduleMapper;
import com.sadiqov.sport_place_app.repo.CategoryRepository;
import com.sadiqov.sport_place_app.repo.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepo;
    private final CategoryRepository categoryRepo;

    @Transactional
    public void createPlace(CreatePlaceRequest request) {
        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Place place = PlaceMapper.toEntity(request, category);

        List<PlaceSchedule> schedules = request.schedules().stream()
                .map(PlaceScheduleMapper::toEntity)
                .peek(schedule -> schedule.setPlace(place))
                .collect(Collectors.toList());

        place.setSchedules(schedules);
        placeRepo.save(place);
        PlaceMapper.toResponse(place);
    }

    @Transactional(readOnly = true)
    public List<PlaceResponse> getAllPlaces() {
        List<Place> places = placeRepo.findAll();
        return PlaceMapper.toResponseList(places);
    }

    @Transactional(readOnly = true)
    public PlaceResponse getPlaceById(Long id) {
        Place place = placeRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Place not found"));
        return PlaceMapper.toResponse(place);
    }

    @Transactional
    public PlaceResponse updatePlace(Long id, CreatePlaceRequest request) {
        Place place = placeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));

        PlaceMapper.updateEntityFromRequest(request, place);

        placeRepo.save(place);
        return PlaceMapper.toResponse(place);
    }

    @Transactional
    public void deletePlace(Long id) {
        Place place = placeRepo.findById(id).orElseThrow
                (() -> new IllegalArgumentException("Place not found"));
        placeRepo.delete(place);
    }
}