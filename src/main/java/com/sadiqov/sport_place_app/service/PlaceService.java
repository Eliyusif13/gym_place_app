package com.sadiqov.sport_place_app.service;

import com.sadiqov.sport_place_app.dto.PlaceDtos;
import com.sadiqov.sport_place_app.entity.Category;
import com.sadiqov.sport_place_app.entity.Place;
import com.sadiqov.sport_place_app.entity.PlaceSchedule;
import com.sadiqov.sport_place_app.repo.CategoryRepository;
import com.sadiqov.sport_place_app.repo.PlaceRepository;
import com.sadiqov.sport_place_app.util.OpeningCalculator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceService {
    private final PlaceRepository placeRepo;
    private final CategoryRepository categoryRepo;

    public PlaceDtos.PlaceResponse create(PlaceDtos.CreatePlaceRequest req) {
        Category cat = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Place p = Place.builder()
                .name(req.name())
                .address(req.address())
                .latitude(req.latitude())
                .longitude(req.longitude())
                .category(cat)
                .score(req.score())
                .description(req.description())
                .phone(req.phone())
                .website(req.website())
                .active(req.active())
                .build();

        p.setSchedules(req.schedules().stream().map(s ->
                PlaceSchedule.builder()
                        .day(s.day())
                        .openTime(s.openTime())
                        .closeTime(s.closeTime())
                        .spansMidnight(s.spansMidnight())
                        .build()
        ).toList());

        Place saved = placeRepo.save(p);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PlaceDtos.PlaceResponse get(Long id) {
        return placeRepo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));
    }

    public PlaceDtos.PlaceResponse update(Long id, PlaceDtos.UpdatePlaceRequest req) {
        Place p = placeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Place not found"));

        if (req.name() != null) p.setName(req.name());
        if (req.address() != null) p.setAddress(req.address());
        if (req.latitude() != null) p.setLatitude(req.latitude());
        if (req.longitude() != null) p.setLongitude(req.longitude());
        if (req.score() != null) p.setScore(req.score());
        if (req.description() != null) p.setDescription(req.description());
        if (req.phone() != null) p.setPhone(req.phone());
        if (req.website() != null) p.setWebsite(req.website());
        if (req.active() != null) p.setActive(req.active());
        if (req.categoryId() != null) {
            Category cat = categoryRepo.findById(req.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            p.setCategory(cat);
        }
        if (req.schedules() != null) {
            p.setSchedules(req.schedules().stream().map(s ->
                    PlaceSchedule.builder()
                            .day(s.day())
                            .openTime(s.openTime())
                            .closeTime(s.closeTime())
                            .spansMidnight(s.spansMidnight())
                            .build()
            ).toList());
        }
        return toResponse(p);
    }

    public void delete(Long id) {
        placeRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<PlaceDtos.PlaceResponse> search(
            String q,
            Long categoryId,
            Boolean openNow,
            Integer minScore,
            BigDecimal lat,
            BigDecimal lng,
            String sortBy,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        List<Place> all = placeRepo.findAll();

        var stream = all.stream()
                .filter(p -> p.getActive() == null || p.getActive())
                .filter(p -> q == null || p.getName().toLowerCase().contains(q.toLowerCase()) ||
                        (p.getAddress() != null && p.getAddress().toLowerCase().contains(q.toLowerCase())))
                .filter(p -> categoryId == null || (p.getCategory() != null && p.getCategory().getId().equals(categoryId)))
                .filter(p -> minScore == null || (p.getScore() != null && p.getScore() >= minScore));

        var list = stream.map(this::toResponse).toList();

        // openNow filtri serverdə hesablansın
        if (openNow != null && openNow) {
            list = list.stream().filter(PlaceDtos.PlaceResponse::openNow).toList();
        }

        // sort
        if ("score".equalsIgnoreCase(sortBy)) {
            list = list.stream().sorted((a, b) -> Integer.compare(
                    b.score() == null ? 0 : b.score(),
                    a.score() == null ? 0 : a.score()
            )).toList();
        } else if ("name".equalsIgnoreCase(sortBy)) {
            list = list.stream().sorted((a, b) -> a.name().compareToIgnoreCase(b.name())).toList();
        } else if ("distance".equalsIgnoreCase(sortBy) && lat != null && lng != null) {
            list = list.stream().sorted(Comparator.comparingDouble(a ->
                    distanceKm(lat.doubleValue(),
                            lng.doubleValue(),
                            a.latitude().doubleValue(),
                            a.longitude().doubleValue()))).toList();
        }

        int from = Math.min(page * size, list.size());
        int to = Math.min(from + size, list.size());
        return new PageImpl<>(list.subList(from, to), pageable, list.size());
    }

    private PlaceDtos.PlaceResponse toResponse(Place p) {
        var scheduleDtos = p.getSchedules().stream()
                .sorted((a, b) -> Integer.compare(a.getDay().getValue(), b.getDay().getValue()))
                .map(s -> new PlaceDtos.ScheduleDto(
                        s.getDay(), s.getOpenTime(), s.getCloseTime(), s.isSpansMidnight()
                )).toList();

        boolean open = OpeningCalculator.isOpenNow(scheduleDtos);
        return new PlaceDtos.PlaceResponse(
                p.getId(), p.getName(), p.getAddress(),
                p.getLatitude(), p.getLongitude(),
                p.getScore(), p.getDescription(), p.getPhone(), p.getWebsite(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                open, scheduleDtos
        );
    }

    private static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
