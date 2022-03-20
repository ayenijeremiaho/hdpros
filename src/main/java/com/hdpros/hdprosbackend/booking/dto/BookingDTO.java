package com.hdpros.hdprosbackend.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingDTO {

    private String jobTitle;

    private LocalDate startDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "HH:mm:ss[.SSS][.SS][.S]")
    private LocalTime startTime;

    private String description;

    private int placeId;

    private boolean jobStatus;

    private List<Long> roomId;

    private String email;

    private Long id;
}
