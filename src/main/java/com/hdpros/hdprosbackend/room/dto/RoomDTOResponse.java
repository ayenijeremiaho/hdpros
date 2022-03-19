package com.hdpros.hdprosbackend.room.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoomDTOResponse {

    private Long id;

    private String roomName;

    private int count;

    private double price;

    private String description;

    private String email;

    private List<String> avatar;

}
