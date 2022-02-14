package com.hdpros.hdprosbackend.places.service;

import com.hdpros.hdprosbackend.places.dto.PlaceDTO;

import java.util.List;

public interface PlaceService {
    PlaceDTO savePlace(PlaceDTO dto);

    PlaceDTO updatePlace(PlaceDTO dto);

    List<PlaceDTO> getPlacesForUser(String email);

    boolean deletePlace(String email, Long placeId);
}
