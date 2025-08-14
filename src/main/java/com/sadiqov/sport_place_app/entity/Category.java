package com.sadiqov.sport_place_app.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "slug"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String name;

    @Column(nullable=false, length=140)
    private String slug;

    private String icon;
}
