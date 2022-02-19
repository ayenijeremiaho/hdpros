package com.hdpros.hdprosbackend.image.service.implementation;

import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.image.repository.ImageRepository;
import com.hdpros.hdprosbackend.image.service.ImageService;
import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image saveImage(String publicId, String url, User user) {
        Image image = createImageObject(publicId, url, user);

        return imageRepository.save(image);
    }

    @Override
    public Image saveProfileImage(String publicId, String url, User user) {
        Image image = createImageObject(publicId, url, user);
        image.setProfileImage(true);

        return imageRepository.save(image);
    }

    private Image createImageObject(String publicId, String url, User user) {
        Image image = new Image();

        image.setPublicId(publicId);
        image.setUrl(url);
        image.setUser(user);
        return image;
    }

    @Override
    public Image saveRoomImage(String publicId, String url, User user, Room room) {
        Image image = createRoomImageObject(publicId, url, user, room);
        image.setRoomImage(true);

        return imageRepository.save(image);
    }

    private Image createRoomImageObject(String publicId, String url, User user, Room room) {
        Image image = new Image();

        image.setPublicId(publicId);
        image.setUrl(url);
        image.setUser(user);
        image.setRoom(room);
        return image;
    }

    @Override
    public Image getImage(String url, Long userId) {
        return imageRepository.findByUrlAndUser_Id(url, userId);
    }

    @Override
    public Image getProfileImage(String url, Long userId) {
        return imageRepository.findByUrlAndProfileImageAndUser_Id(url, true, userId);
    }

//    @Override
//    public Image getRoomImage(String url, Long userId, Long roomId) {
//        return imageRepository.findUrlAndRoomImageAndUser_Id(url, true, userId, roomId);
//    }
}
