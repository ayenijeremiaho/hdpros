package com.hdpros.hdprosbackend.booking.service.implementation;

import com.hdpros.hdprosbackend.booking.dto.BookingDTO;
import com.hdpros.hdprosbackend.booking.dto.BookingDTOResponse;
import com.hdpros.hdprosbackend.booking.model.Booking;
import com.hdpros.hdprosbackend.booking.repository.BookingRepository;
import com.hdpros.hdprosbackend.booking.service.BookingService;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.places.dto.PlaceDTO;
import com.hdpros.hdprosbackend.places.service.PlaceService;
import com.hdpros.hdprosbackend.room.dto.RoomDTOResponse;
import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.room.service.RoomService;
import com.hdpros.hdprosbackend.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final RoomService roomService;
    private final PlaceService placeService;
    private final GeneralService generalService;
    private final BookingRepository bookingRepository;
    @Value("${record.count}")
    private int count;

    public BookingServiceImpl(RoomService roomService, PlaceService placeService, GeneralService generalService, BookingRepository bookingRepository) {
        this.roomService = roomService;
        this.placeService = placeService;
        this.generalService = generalService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDTOResponse saveBooking(BookingDTO dto) {
        log.info("Saving booking for user");

        User user = generalService.getUser(dto.getEmail());

        if (bookingRepository.countAllByUserAndDelFlag(user, false) > count) {
            throw new GeneralException("User can only save upto " + count + " bookings");
        }

        BookingDTOResponse response = new BookingDTOResponse();

        if (!bookingRepository.existsByDescriptionAndUserAndDelFlag(dto.getDescription(), user, false)) {

            log.info("Saving new booking for user => {}", user.getEmail());
            Booking booking = new Booking();
            BeanUtils.copyProperties(dto, booking);
            booking.setUser(user);
            booking.setCreatedAt(LocalDateTime.now());

            //get room info
            List<Room> rooms = getRooms(dto.getRoomId(), user);

            //get place info
            PlaceDTO place = placeService.getPlaceDTO(placeService.getPlace(booking.getUser(), booking.getPlaceId()));

            booking.setRooms(rooms);
            booking = bookingRepository.save(booking);

            //get id
            BeanUtils.copyProperties(dto, response);
            response.setId(booking.getId());
            response.setJobStatus("Pending");
            response.setPlace(place);

            //convert to room response
            List<RoomDTOResponse> roomDTOResponses = getRoomsDTO(rooms);

            //get total price of rooms for bookings
            double sum = roomDTOResponses.stream().filter(o -> o.getPrice() > 10).mapToDouble(RoomDTOResponse::getPrice).sum();

            response.setRooms(roomDTOResponses);
            response.setAmount(sum);

            return response;
        }
        throw new GeneralException("booking with description already created for user");
    }

    private List<RoomDTOResponse> getRoomsDTO(List<Room> rooms) {
        return rooms.stream().map(roomService::getRoomDTOResponse).collect(Collectors.toList());
    }

    private List<Room> getRooms(List<Long> roomIds, User user) {
        return roomIds.stream().map(roomId -> roomService.getRoom(user, roomId)).collect(Collectors.toList());
    }

    @Override
    public BookingDTOResponse updateBooking(BookingDTO dto) {
        log.info("Updating booking for user");

        User user = generalService.getUser(dto.getEmail());

        BookingDTOResponse response = new BookingDTOResponse();

        Booking booking = getBooking(user, dto.getId());

        booking.setJobTitle(dto.getJobTitle());
        booking.setDescription(dto.getDescription());
        booking.setPlaceId(dto.getPlaceId());
        booking.setStartDate(dto.getStartDate());
        booking.setStartTime(dto.getStartTime());
        booking.setJobStatus(dto.isJobStatus());

        List<Room> rooms = getRooms(dto.getRoomId(), user);

        booking.setRooms(rooms);
        booking.setUpdatedAt(LocalDateTime.now());

        Booking updateBooking = bookingRepository.save(booking);

        //get dto properties into DTOResponse
        BeanUtils.copyProperties(dto, response);

        //get room info
        List<RoomDTOResponse> roomDTOResponses = getRoomsDTO(rooms);

        //get total price of rooms for bookings
        double sum = roomDTOResponses.stream().filter(o -> o.getPrice() > 10).mapToDouble(RoomDTOResponse::getPrice).sum();

        //get place info
        PlaceDTO place = placeService.getPlaceDTO(placeService.getPlace(booking.getUser(), booking.getPlaceId()));

        response.setPlace(place);
        response.setRooms(roomDTOResponses);
        response.setAmount(sum);

        return response;
    }

    @Override
    public List<BookingDTOResponse> getBookingForUser(String email) {
        log.info("Getting bookings for user");

        User user = generalService.getUser(email);

        List<Booking> bookings = bookingRepository.findByUserAndDelFlag(user, false);

        return bookings.stream().map(this::getBookingDTOResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingDTOResponse> getBookingForUserByStatus(String email, String statusParam) {
        log.info("Getting bookings for user");

        User user = generalService.getUser(email);

        List<Booking> bookings = getBookingByJobStatus(user, statusParam);

        return bookings.stream().map(this::getBookingDTOResponse).collect(Collectors.toList());
    }

    @Override
    public boolean deleteBooking(String email, Long bookingId) {
        log.info("Deleting booking for user");

        User user = generalService.getUser(email);

        Booking booking = getBooking(user, bookingId);

        booking.setDelFlag(true);
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public boolean updateBookingJobStatus(String email, Long bookingId, String statusParam) {
        log.info("Updating booking job status for user");

        User user = generalService.getUser(email);

        //confirm user is not a service provider

        Booking booking = getBooking(user, bookingId);

        if (Objects.equals(statusParam, "pending")) {
            booking.setJobStatus(false);
        } else if (Objects.equals(statusParam, "done")) {
            booking.setJobStatus(true);
        } else if (Objects.equals(statusParam, "paid")) {
            booking.setPaid(true);
        } else if (Objects.equals(statusParam, "accepted")) {
            booking.setAccepted(true);
        } else {
            booking.setJobStatus(booking.isJobStatus());
            booking.setAccepted(booking.isAccepted());
            booking.setPaid(booking.isPaid());
        }
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public BookingDTOResponse getSingleBookingForUser(String email, Long bookingId) {
        log.info("Getting Single booking for user");

        User user = generalService.getUser(email);

        BookingDTO dto = new BookingDTO();

        BookingDTOResponse response = getBookingDTOResponse(getBooking(user, bookingId));

        BeanUtils.copyProperties(response, dto);

        //get id
        response.setId(response.getId());
        response.setEmail(email);

        return response;
    }

    private Booking getBooking(User user, Long bookingId) {
        log.info("Getting booking for user");

        Booking booking = bookingRepository.findByUserAndIdAndDelFlag(user, bookingId, false);
        if (Objects.isNull(booking)) {
            throw new GeneralException("Invalid Request");
        }
        return booking;
    }

    private List<Booking> getBookingByJobStatus(User user, String statusParam) {
        log.info("Getting booking for user by job status");
        List<Booking> booking = null;
        if (Objects.equals(statusParam, "pending")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAccepted(user, false, false, false, false);
        } else if (Objects.equals(statusParam, "done")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAccepted(user, false, true, true, true);
        } else if (Objects.equals(statusParam, "paid")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAccepted(user, false, false, true, true);
        } else if (Objects.equals(statusParam, "accepted")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAccepted(user, false, false, false, true);
        } else {
            booking = Collections.emptyList();
        }

        if (Objects.isNull(booking)) {
            throw new GeneralException("Invalid Request");
        }

        return booking;
    }

    private BookingDTO getBookingDTO(Booking booking) {
        log.info("Converting Booking to Booking DTO");

        BookingDTO bookingDTO = new BookingDTO();
        BeanUtils.copyProperties(booking, bookingDTO);
        return bookingDTO;
    }

    private BookingDTOResponse getBookingDTOResponse(Booking booking) {
        log.info("Converting Booking to Booking DTO Response");

        BookingDTOResponse bookingDTOResponse = new BookingDTOResponse();
        BeanUtils.copyProperties(booking, bookingDTOResponse);

        if (Objects.equals(booking.isJobStatus(), true) && Objects.equals(booking.isAccepted(), true) && Objects.equals(booking.isPaid(), true)) {
            bookingDTOResponse.setJobStatus("Done");
        } else if (Objects.equals(booking.isPaid(), true) && Objects.equals(booking.isAccepted(), true)) {
            bookingDTOResponse.setJobStatus("Paid");
        } else if (Objects.equals(booking.isAccepted(), true)) {
            bookingDTOResponse.setJobStatus("Accepted");
        } else {
            bookingDTOResponse.setJobStatus("Pending");
        }

        List<RoomDTOResponse> roomsDtoResponses = booking.getRooms().stream()
                .map(roomService::getRoomDTOResponse).collect(Collectors.toList());

        double sum = roomsDtoResponses.stream().filter(o -> o.getPrice() > 10).mapToDouble(RoomDTOResponse::getPrice).sum();

        PlaceDTO place = placeService.getPlaceDTO(placeService.getPlace(booking.getUser(), booking.getPlaceId()));

        bookingDTOResponse.setRooms(roomsDtoResponses);
        bookingDTOResponse.setEmail(booking.getUser().getEmail());
        bookingDTOResponse.setPlace(place);
        bookingDTOResponse.setAmount(sum);

        return bookingDTOResponse;
    }
}
