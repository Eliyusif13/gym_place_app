package com.sadiqov.sport_place_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=180)
    private String name;

    @Column(length=300)
    private String address;

    @Column(precision = 10, scale = 7, nullable=false)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7, nullable=false)
    private BigDecimal longitude;

    private Integer score;

    @Column(length=500)
    private String description;

    private String phone;
    private String website;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceSchedule> schedules = new ArrayList<>();

    private Boolean active;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public void setSchedules(List<PlaceSchedule> schedules) {
        this.schedules.clear();
        if (schedules != null) {
            this.schedules.addAll(schedules);
        }
    }

}