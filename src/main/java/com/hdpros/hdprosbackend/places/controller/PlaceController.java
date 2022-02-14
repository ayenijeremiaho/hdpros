package com.hdpros.hdprosbackend.places.controller;

import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.places.dto.PlaceDTO;
import com.hdpros.hdprosbackend.places.service.PlaceService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;
    private final GeneralService generalService;

    public PlaceController(PlaceService placeService, GeneralService generalService) {
        this.placeService = placeService;
        this.generalService = generalService;
    }

    @PostMapping("/add")
    public Response addPlace(Principal principal, @RequestBody PlaceDTO placeDTO) {
        //update the email of the user to that of the logged-in user
        placeDTO.setEmail(principal.getName());

        PlaceDTO response = placeService.savePlace(placeDTO);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/update")
    public Response updatePlace(Principal principal, @RequestBody PlaceDTO placeDTO) {
        //update the email of the user to that of the logged-in user
        placeDTO.setEmail(principal.getName());

        PlaceDTO response = placeService.updatePlace(placeDTO);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/delete/{placeId}")
    public Response deletePlace(Principal principal, @PathVariable Long placeId) {
        boolean response = placeService.deletePlace(principal.getName(), placeId);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/all")
    public Response allPlaces(Principal principal) {
        List<PlaceDTO> response = placeService.getPlacesForUser(principal.getName());

        return generalService.prepareSuccessResponse(response);
    }


}
