package com.hdpros.hdprosbackend.places.repository;

import com.hdpros.hdprosbackend.places.model.Place;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsByDescriptionAndUserAndDelFlag(String description, User user, boolean delFlag);

    int countAllByUserAndDelFlag(User user, boolean delFlag);

    List<Place> findByUserAndDelFlag(User user, boolean delFlag);

    Place findByUserAndIdAndDelFlag(User user, Long id, boolean delFlag);
}
