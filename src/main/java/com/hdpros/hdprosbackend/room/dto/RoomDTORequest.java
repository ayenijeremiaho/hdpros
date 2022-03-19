package com.hdpros.hdprosbackend.room.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class RoomDTORequest {

    private String roomName;

    private int count;

    private double price;

    private String description;

    private String email;

    private List<String> avatar;
}
