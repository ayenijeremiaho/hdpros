package com.hdpros.hdprosbackend.room.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RoomDTOResponse {

    private Long id;

    private String roomName;

    private int count;

    private double price;

    private String description;

    private String email;

<<<<<<< HEAD:src/main/java/com/hdpros/hdprosbackend/room/dto/RoomDTO.java
    private Long id;

//    private List<String> avatar;

    private String avatar;

    @JsonIgnore
    private MultipartFile file;

//    @JsonIgnore
//    private List<MultipartFile> file;
=======
    private List<String> avatar;

>>>>>>> master:src/main/java/com/hdpros/hdprosbackend/room/dto/RoomDTOResponse.java
}
