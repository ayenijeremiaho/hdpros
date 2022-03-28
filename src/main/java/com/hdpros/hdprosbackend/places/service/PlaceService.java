package com.hdpros.hdprosbackend.places.service;

import com.hdpros.hdprosbackend.places.dto.PlaceDTO;
import com.hdpros.hdprosbackend.places.model.Place;
import com.hdpros.hdprosbackend.user.model.User;

import java.util.List;

public interface PlaceService {
    PlaceDTO savePlace(PlaceDTO dto);

    PlaceDTO updatePlace(PlaceDTO dto);

    List<PlaceDTO> getPlacesForUser(String email);

    boolean deletePlace(String email, Long placeId);

    Place getPlace(User user, Long placeId);

    PlaceDTO getPlaceDTO(Place place);
}
