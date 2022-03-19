package com.hdpros.hdprosbackend.room.service;

import com.hdpros.hdprosbackend.room.dto.RoomDTORequest;
import com.hdpros.hdprosbackend.room.dto.RoomDTOResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {

    List<MultipartFile> convertToMultipart(List<String> base64);

    RoomDTOResponse saveRoom(RoomDTORequest dto);

    RoomDTOResponse updateRoom(RoomDTORequest dto);

    List<RoomDTORequest> getRoomForUser(String email);

    boolean deleteRoom(String email, Long roomId);
}
