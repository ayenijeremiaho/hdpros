package com.hdpros.hdprosbackend.room.service;

import com.hdpros.hdprosbackend.room.dto.RoomDTO;

import java.util.List;

public interface RoomService {
    RoomDTO saveRoom(RoomDTO dto);

    RoomDTO updateRoom(RoomDTO dto);

    List<RoomDTO> getRoomForUser(String email);

    boolean deleteRoom(String email, Long roomId);
}
