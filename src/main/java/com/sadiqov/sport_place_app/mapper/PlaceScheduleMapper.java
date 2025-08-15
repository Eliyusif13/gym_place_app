package com.sadiqov.sport_place_app.mapper;

import com.sadiqov.sport_place_app.dto.PlaceDtos.ScheduleDto;
import com.sadiqov.sport_place_app.entity.PlaceSchedule;

public class PlaceScheduleMapper {

    public static PlaceSchedule toEntity(ScheduleDto scheduleDto) {
        PlaceSchedule placeSchedule = new PlaceSchedule();
        placeSchedule.setDay(scheduleDto.day());
        placeSchedule.setOpenTime(scheduleDto.openTime());
        placeSchedule.setCloseTime(scheduleDto.closeTime());
        placeSchedule.setSpansMidnight(scheduleDto.spansMidnight());
        return placeSchedule;
    }
}
