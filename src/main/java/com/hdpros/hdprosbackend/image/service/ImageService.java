package com.hdpros.hdprosbackend.image.service;

import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.user.model.User;

public interface ImageService {

    Image saveImage(String publicId, String url,  User user);

    Image saveProfileImage(String publicId, String url, User user);

    Image getImage(String url, Long userId);

    Image getProfileImage(Long userId);

    Image saveRoomImage(String publicId, String url, User user, Room room);

//    Image getRoomImage(String url, Long userId, Long roomId);

}
