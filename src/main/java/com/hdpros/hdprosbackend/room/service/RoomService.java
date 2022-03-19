package com.hdpros.hdprosbackend.room.service;

import com.hdpros.hdprosbackend.room.dto.RoomDTORequest;
import com.hdpros.hdprosbackend.room.dto.RoomDTOResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {

    List<MultipartFile> convertToMultipart(List<String> base64);

<<<<<<< HEAD
    MultipartFile convertOneToMultipart(String base64);

    RoomDTO saveRoom(RoomDTO dto);
=======
    RoomDTOResponse saveRoom(RoomDTORequest dto);
>>>>>>> master

    RoomDTOResponse updateRoom(RoomDTORequest dto);

    List<RoomDTOResponse> getRoomForUser(String email);

    boolean deleteRoom(String email, Long roomId);
}
