package com.sadiqov.sport_place_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "place_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="place_id")
    private Place place;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=12)
    private DayOfWeek day;

    @Column(nullable=false)
    private LocalTime openTime;

    @Column(nullable=false)
    private LocalTime closeTime;

    @Column(nullable=false)
    private boolean spansMidnight;
}