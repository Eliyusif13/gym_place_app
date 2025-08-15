package com.sadiqov.sport_place_app.dto;

import com.sadiqov.sport_place_app.entity.PlaceSchedule;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public final class PlaceDtos {
    public record ScheduleDto(
            @NotNull DayOfWeek day,
            @NotNull LocalTime openTime,
            @NotNull LocalTime closeTime,
            boolean spansMidnight
    ) {
    }

    public record CreatePlaceRequest(
            @NotBlank String name,
            String address,
            @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
            @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude,
            @NotNull Long categoryId,
            @Min(0) Integer score,
            String description,
            String phone,
            String website,
            @NotNull List<@NotNull ScheduleDto> schedules,
            @NotNull Boolean active
    ) {
    }


    public record PlaceResponse(
            Long id,
            String name,
            String address,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer score,
            String description,
            String phone,
            String website,
            Long categoryId,
            String categoryName,
            boolean openNow,
            List<PlaceSchedule> schedules
    ) {
    }

    public record CreateCategoryRequest(String name, String slug, String icon) {
    }

    public record CategoryResponse(Long id, String name, String slug, String icon) {
    }
}
