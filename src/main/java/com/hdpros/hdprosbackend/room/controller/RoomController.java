package com.hdpros.hdprosbackend.room.controller;

import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.room.dto.RoomDTO;
import com.hdpros.hdprosbackend.room.service.RoomService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;
    private final GeneralService generalService;

    public RoomController(RoomService roomService, GeneralService generalService) {
        this.roomService = roomService;
        this.generalService = generalService;
    }

    @PostMapping("/add")
    public Response addRoom(@ApiIgnore Principal principal, @RequestBody RoomDTO roomDTO) {
        //update the email of the user to that of the logged-in user
        roomDTO.setEmail(principal.getName());

        RoomDTO response = roomService.saveRoom(roomDTO);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/update")
    public Response updateRoom(@ApiIgnore Principal principal, @RequestBody RoomDTO roomDTO) {
        //update the email of the user to that of the logged-in user
        roomDTO.setEmail(principal.getName());

        RoomDTO response = roomService.updateRoom(roomDTO);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/delete/{RoomId}")
    public Response deleteRoom(@ApiIgnore Principal principal, @PathVariable Long roomId) {
        boolean response = roomService.deleteRoom(principal.getName(), roomId);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/all")
    public Response allRooms(@ApiIgnore Principal principal) {
        List<RoomDTO> response = roomService.getRoomForUser(principal.getName());

        return generalService.prepareSuccessResponse(response);
    }

}