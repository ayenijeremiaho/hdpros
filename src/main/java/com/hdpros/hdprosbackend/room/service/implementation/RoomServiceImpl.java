package com.hdpros.hdprosbackend.room.service.implementation;

import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.image.service.ImageService;
import com.hdpros.hdprosbackend.providers.cloudinary.CloudinaryService;
import com.hdpros.hdprosbackend.room.dto.RoomDTO;
import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.room.repository.RoomRepository;
import com.hdpros.hdprosbackend.room.service.RoomService;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.utils.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final GeneralService generalService;
    private final RoomRepository roomRepository;
    private final ImageService imageService;
    private final CloudinaryService cloudinaryService;

    public RoomServiceImpl(GeneralService generalService, RoomRepository roomRepository, ImageService imageService, CloudinaryService cloudinaryService) {
        this.generalService = generalService;
        this.roomRepository = roomRepository;
        this.imageService = imageService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public List<MultipartFile> convertToMultipart(List<String> base64) {
        if (GeneralUtil.listIsEmpty(base64)) {
            return null;
        }

        List<MultipartFile> file = GeneralUtil.base64ToMultipartList(base64);
        if (Objects.isNull(file)) {
            throw new GeneralException("Invalid image, please re-upload");
        }
        return file;
    }

    @Override
    public MultipartFile convertOneToMultipart(String base64) {
        if (GeneralUtil.stringIsNullOrEmpty(base64)) {
            return null;
        }

        MultipartFile file = GeneralUtil.base64ToMultipart(base64);
        if (Objects.isNull(file)) {
            throw new GeneralException("Invalid image, please re-upload");
        }
        return file;
    }

    @Override
    public RoomDTO saveRoom(RoomDTO dto) {
        log.info("Saving room for user");

        User user = generalService.getUser(dto.getEmail());

        if (roomRepository.countAllByUserAndDelFlag(user, false) > 5) {
            throw new GeneralException("User can only save upto 5 room");
        }

        if (!roomRepository.existsByDescriptionAndUserAndDelFlag(dto.getDescription(), user, false)) {

            log.info("Saving new room for user => {}", user.getEmail());
            Room room = new Room();
            BeanUtils.copyProperties(dto, room);
            room.setUser(user);
            room.setCreatedAt(LocalDateTime.now());

            roomRepository.save(room);

            //verify image was uploaded
            if (Objects.nonNull(dto.getFile())) {
                Map<String, String> imageMap = cloudinaryService.upload(dto.getFile());
//                Map<String, String> imageMap = cloudinaryService.upload((MultipartFile) dto.getFile());

                if (Objects.nonNull(imageMap)) {
                    String publicId = imageMap.get("publicId");
                    String imageUrl = imageMap.get("url");

                    //save profile image
                    Image image = imageService.saveRoomImage(publicId, imageUrl, user, room);
                    room.setImages(image);
                    roomRepository.save(room);
                }
            }
            return dto;
        }
        throw new GeneralException("room with description already created for user");
    }

    @Override
    public RoomDTO updateRoom(RoomDTO dto) {
        log.info("Updating room for user");

        User user = generalService.getUser(dto.getEmail());

        Room room = getRoom(user, dto.getId());

        room.setCount(dto.getCount());
        room.setDescription(dto.getDescription());
        room.setRoomName(dto.getRoomName());
        room.setPrice(dto.getPrice());
        room.setUpdatedAt(LocalDateTime.now());

        Room updatedRoom = roomRepository.save(room);

        //verify image was uploaded
        if (Objects.nonNull(dto.getFile())) {
            Map<String, String> imageMap = cloudinaryService.upload(dto.getFile());
//            Map<String, String> imageMap = cloudinaryService.upload((MultipartFile) dto.getFile());

            if (Objects.nonNull(imageMap)) {
                String publicId = imageMap.get("publicId");
                String imageUrl = imageMap.get("url");

                //save profile image
                Image image = imageService.saveRoomImage(publicId, imageUrl, user, room);
                room.setImages(image);
                roomRepository.save(room);
            }
        }
        return getRoomDTO(updatedRoom);
    }


    @Override
    public List<RoomDTO> getRoomForUser(String email) {
        log.info("Getting rooms for user");

        User user = generalService.getUser(email);

        List<Room> rooms = roomRepository.findByUserAndDelFlag(user, false);

        return rooms.stream().map(this::getRoomDTO).collect(Collectors.toList());
    }

    @Override
    public boolean deleteRoom(String email, Long roomId) {
        log.info("Deleting room for user");

        User user = generalService.getUser(email);

        Room room = getRoom(user, roomId);

        room.setDelFlag(true);
        roomRepository.save(room);
        return true;
    }

    private Room getRoom(User user, Long roomId) {
        log.info("Getting Room for user");

        Room room = roomRepository.findByUserAndIdAndDelFlag(user, roomId, false);
        if (Objects.isNull(room)) {
            throw new GeneralException("Invalid Request");
        }
        return room;
    }

    private RoomDTO getRoomDTO(Room room) {
        log.info("Converting Room to Room DTO");

        RoomDTO roomDTO = new RoomDTO();
        BeanUtils.copyProperties(room, roomDTO);
        return roomDTO;
    }

}
