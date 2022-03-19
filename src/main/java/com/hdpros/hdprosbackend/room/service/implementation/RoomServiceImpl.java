package com.hdpros.hdprosbackend.room.service.implementation;

import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.image.service.ImageService;
import com.hdpros.hdprosbackend.providers.cloudinary.CloudinaryService;
import com.hdpros.hdprosbackend.room.dto.RoomDTORequest;
import com.hdpros.hdprosbackend.room.dto.RoomDTOResponse;
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
import java.util.ArrayList;
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

        List<MultipartFile> files = new ArrayList<>();

        base64.forEach(s -> {
            MultipartFile file = GeneralUtil.base64ToMultipart(s);
            if (Objects.isNull(file)) {
                throw new GeneralException("Invalid image, please re-upload");
            }
            files.add(file);
        });

        return files;
    }

    @Override
<<<<<<< HEAD
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
=======
    public RoomDTOResponse saveRoom(RoomDTORequest dto) {
>>>>>>> master
        log.info("Saving room for user");

        User user = generalService.getUser(dto.getEmail());

        if (roomRepository.countAllByUserAndDelFlag(user, false) > 5) {
            throw new GeneralException("User can only save upto 5 room");
        }

        if (!roomRepository.existsByDescriptionAndUserAndDelFlag(dto.getDescription(), user, false)) {


            List<String> imagesUrl = new ArrayList<>();

            log.info("Saving new room for user => {}", user.getEmail());
            Room room = new Room();
            BeanUtils.copyProperties(dto, room);
            room.setUser(user);
            room.setCreatedAt(LocalDateTime.now());

            room = roomRepository.save(room);

            //verify image was uploaded
<<<<<<< HEAD
<<<<<<< HEAD
            if (Objects.nonNull(dto.getFile())) {
                Map<String, String> imageMap = cloudinaryService.upload(dto.getFile());
//                Map<String, String> imageMap = cloudinaryService.upload((MultipartFile) dto.getFile());
=======
            if (!dto.getFile().isEmpty()) {
=======
            if (!dto.getAvatar().isEmpty()) {
>>>>>>> master
                List<Image> images = new ArrayList<>();
>>>>>>> master

                uploadImage(dto, user, imagesUrl, images);

                room.setImages(images);
                room = roomRepository.save(room);
            }

            //get response object
            RoomDTOResponse response = getRoomDTOResponse(room, dto);
            response.setAvatar(imagesUrl);
            return response;
        }
        throw new GeneralException("room with description already created for user");
    }


    @Override
    public RoomDTOResponse updateRoom(RoomDTORequest dto) {
        log.info("Updating room for user");

        User user = generalService.getUser(dto.getEmail());

        Room room = getRoom(user, dto.getId());

        List<String> imagesUrl = new ArrayList<>();

        List<Image> images = new ArrayList<>();

        room.setCount(dto.getCount());
        room.setDescription(dto.getDescription());
        room.setRoomName(dto.getRoomName());
        room.setPrice(dto.getPrice());
        room.setUpdatedAt(LocalDateTime.now());

        Room updatedRoom = roomRepository.save(room);

<<<<<<< HEAD
        //verify image was uploaded
        if (Objects.nonNull(dto.getFile())) {
            Map<String, String> imageMap = cloudinaryService.upload(dto.getFile());
//            Map<String, String> imageMap = cloudinaryService.upload((MultipartFile) dto.getFile());
=======
        uploadImage(dto, user, imagesUrl, images);
>>>>>>> master

        //get response object
        RoomDTOResponse response = getRoomDTOResponse(updatedRoom, dto);
        response.setAvatar(imagesUrl);
        return response;
    }


    @Override
    public List<RoomDTORequest> getRoomForUser(String email) {
        log.info("Getting rooms for user");

        User user = generalService.getUser(email);

        List<Room> rooms = roomRepository.findByUserAndDelFlag(user, false);

        return rooms.stream().map(this::getRoomDTORequest).collect(Collectors.toList());
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

    private RoomDTORequest getRoomDTORequest(Room room) {
        log.info("Converting Room to Room DTO");

        RoomDTORequest roomDTORequest = new RoomDTORequest();
        BeanUtils.copyProperties(room, roomDTORequest);
        return roomDTORequest;
    }

    private RoomDTOResponse getRoomDTOResponse(Room room, RoomDTORequest dtoRequest) {
        log.info("Converting Room to Room DTO");

        RoomDTOResponse dtoResponse = new RoomDTOResponse();
        BeanUtils.copyProperties(dtoRequest, dtoResponse);
        dtoResponse.setEmail(room.getUser().getEmail());
        dtoResponse.setId(room.getId());
        return dtoResponse;
    }

    private void uploadImage(RoomDTORequest dto, User user, List<String> imagesUrl, List<Image> images) {
        dto.getAvatar().forEach(s -> {

            MultipartFile file = GeneralUtil.base64ToMultipart(s);
            if (Objects.isNull(file)) {
                throw new GeneralException("Invalid image, please re-upload");
            }

            Map<String, String> imageMap = cloudinaryService.upload(file);

            if (Objects.nonNull(imageMap)) {
                String publicId = imageMap.get("publicId");
                String imageUrl = imageMap.get("url");

                //save profile image
                Image image = imageService.saveImage(publicId, imageUrl, user);
                images.add(image);

                //add images url to response
                imagesUrl.add(imageUrl);
            }
        });
    }

}
