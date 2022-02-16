package com.hdpros.hdprosbackend.room.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RoomDTO {

    private String roomName;

    private int count;

    private double price;

    private String description;

    private String email;

    private Long id;

    private MultipartFile file;
}
