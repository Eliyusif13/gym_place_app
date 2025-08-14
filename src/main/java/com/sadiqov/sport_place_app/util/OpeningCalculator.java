package com.sadiqov.sport_place_app.util;


import com.sadiqov.sport_place_app.dto.PlaceDtos;
import com.sadiqov.sport_place_app.dto.PlaceDtos.ScheduleDto;

import java.time.*;

public class OpeningCalculator {

    public static boolean isOpenNow(Iterable<PlaceDtos.ScheduleDto> schedules) {
        ZoneId zone = ZoneId.of("Asia/Baku");
        ZonedDateTime now = ZonedDateTime.now(zone);
        DayOfWeek today = now.getDayOfWeek();
        LocalTime t = now.toLocalTime();

        for (ScheduleDto s : schedules) {
            if (s.day() != today) continue;

            if (!s.spansMidnight()) {
                if (!t.isBefore(s.openTime()) && t.isBefore(s.closeTime())) return true;
            } else {
                if (!t.isBefore(s.openTime()) || t.isBefore(s.closeTime())) return true;
            }
        }
        return false;
    }
}
