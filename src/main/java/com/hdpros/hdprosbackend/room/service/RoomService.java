package com.hdpros.hdprosbackend.room.service;

import com.hdpros.hdprosbackend.room.dto.RoomDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {

    List<MultipartFile> convertToMultipart(List<String> base64);

    RoomDTO saveRoom(RoomDTO dto);

    RoomDTO updateRoom(RoomDTO dto);

    List<RoomDTO> getRoomForUser(String email);

    boolean deleteRoom(String email, Long roomId);
}
