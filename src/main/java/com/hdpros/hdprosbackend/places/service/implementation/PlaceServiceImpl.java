package com.hdpros.hdprosbackend.places.service.implementation;

import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.places.dto.PlaceDTO;
import com.hdpros.hdprosbackend.places.model.Place;
import com.hdpros.hdprosbackend.places.repository.PlaceRepository;
import com.hdpros.hdprosbackend.places.service.PlaceService;
import com.hdpros.hdprosbackend.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlaceServiceImpl implements PlaceService {

    private final GeneralService generalService;
    private final PlaceRepository placeRepository;
    @Value("${record.count}")
    private int count;

    public PlaceServiceImpl(GeneralService generalService, PlaceRepository placeRepository) {
        this.generalService = generalService;
        this.placeRepository = placeRepository;
    }

    @Override
    public PlaceDTO savePlace(PlaceDTO dto) {
        log.info("Saving place for user");

        User user = generalService.getUser(dto.getEmail());

        if (placeRepository.countAllByUserAndDelFlag(user, false) > count) {
            throw new GeneralException("User can only save upto 5 places");
        }

        if (!placeRepository.existsByDescriptionAndUserAndDelFlag(dto.getDescription(), user, false)) {

            log.info("Saving new place for user => {}", user.getEmail());
            Place place = new Place();
            BeanUtils.copyProperties(dto, place);
            place.setUser(user);
            place.setCreatedAt(LocalDateTime.now());

            placeRepository.save(place);
            return dto;
        }
        throw new GeneralException("Place with description already created for user");
    }

    @Override
    public PlaceDTO updatePlace(PlaceDTO dto) {
        log.info("Updating place for user");

        User user = generalService.getUser(dto.getEmail());

        Place place = getPlace(user, dto.getId());

        place.setAddress(dto.getAddress());
        place.setState(dto.getState());
        place.setLandMark(dto.getLandMark());
        place.setUpdatedAt(LocalDateTime.now());

        Place updatedPlace = placeRepository.save(place);
        return getPlaceDTO(updatedPlace);
    }


    @Override
    public List<PlaceDTO> getPlacesForUser(String email) {
        log.info("Getting places for user");

        User user = generalService.getUser(email);

        List<Place> places = placeRepository.findByUserAndDelFlag(user, false);

        return places.stream().map(this::getPlaceDTO).collect(Collectors.toList());
    }

    @Override
    public boolean deletePlace(String email, Long placeId) {
        log.info("Deleting place for user");

        User user = generalService.getUser(email);

        Place place = getPlace(user, placeId);

        place.setDelFlag(true);
        placeRepository.save(place);
        return true;
    }

    @Override
    public Place getPlace(User user, Long placeId) {
        log.info("Getting place for user");

        Place place = placeRepository.findByUserAndIdAndDelFlag(user, placeId, false);
        if (Objects.isNull(place)) {
            throw new GeneralException("Invalid Request");
        }
        return place;
    }

    @Override
    public PlaceDTO getPlaceDTO(Place place) {
        log.info("Converting Place to Place DTO");

        PlaceDTO placeDTO = new PlaceDTO();
        BeanUtils.copyProperties(place, placeDTO);

        //set email
        placeDTO.setEmail(place.getUser().getEmail());
        System.out.println("here: " + place.getUser().getEmail());
        return placeDTO;
    }
}
