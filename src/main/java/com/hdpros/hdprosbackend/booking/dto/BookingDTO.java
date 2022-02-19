package com.hdpros.hdprosbackend.booking.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingDTO {

    private String jobTitle;

    private LocalDate startDate;

    private LocalTime startTime;

    private String description;

    private int placeId;

    private boolean jobStatus;

    private List<Long> roomId;

    private String email;

    private Long id;
}
