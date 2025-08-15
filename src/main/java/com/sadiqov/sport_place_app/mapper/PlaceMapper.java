package com.sadiqov.sport_place_app.mapper;

import com.sadiqov.sport_place_app.dto.PlaceDtos.*;
import com.sadiqov.sport_place_app.entity.Category;
import com.sadiqov.sport_place_app.entity.Place;
import com.sadiqov.sport_place_app.util.OpeningCalculator;


import java.util.List;
import java.util.stream.Collectors;

public class PlaceMapper {

    public static Place toEntity(CreatePlaceRequest request, Category category) {
        return Place.builder()
                .name(request.name())
                .address(request.address())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .score(request.score())
                .description(request.description())
                .phone(request.phone())
                .website(request.website())
                .active(request.active())
                .category(category)
                .build();
    }
    public static PlaceResponse toResponse(Place place) {
        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getScore(),
                place.getDescription(),
                place.getPhone(),
                place.getWebsite(),
                place.getCategory().getId(),
                place.getCategory().getName(),
                OpeningCalculator.isOpenNow(place.getSchedules()),  // Düzgün açılış saatını yoxlayın
                place.getSchedules()
        );
    }

    public static List<PlaceResponse> toResponseList(List<Place> places) {
        return places.stream()
                .map(PlaceMapper::toResponse)
                .collect(Collectors.toList());
    }
    public static void updateEntityFromRequest(CreatePlaceRequest request, Place place) {
        place.setName(request.name());
        place.setAddress(request.address());
        place.setLatitude(request.latitude());
        place.setLongitude(request.longitude());
        place.setScore(request.score());
        place.setDescription(request.description());
        place.setPhone(request.phone());
        place.setWebsite(request.website());
        place.setActive(request.active());
    }
}
