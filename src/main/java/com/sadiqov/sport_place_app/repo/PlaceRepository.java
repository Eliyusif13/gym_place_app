package com.sadiqov.sport_place_app.repo;
import com.sadiqov.sport_place_app.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlaceRepository extends JpaRepository<Place, Long> {

}