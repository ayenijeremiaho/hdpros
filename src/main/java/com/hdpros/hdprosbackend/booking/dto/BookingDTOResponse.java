package com.hdpros.hdprosbackend.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hdpros.hdprosbackend.places.dto.PlaceDTO;
import com.hdpros.hdprosbackend.room.dto.RoomDTOResponse;
import com.hdpros.hdprosbackend.user.dto.ProviderResponse;
import com.hdpros.hdprosbackend.user.dto.UserResponse;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingDTOResponse {

    private String jobTitle;

    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss[.SSS][.SS][.S]")
    private LocalTime startTime;

    private String description;

    private PlaceDTO place;

    private String jobStatus = "Pending";

    private List<RoomDTOResponse> rooms;

    private String email;

    private Long id;

    private double amount = 0.00;

    private ProviderResponse provider;

    private UserResponse user;
}
