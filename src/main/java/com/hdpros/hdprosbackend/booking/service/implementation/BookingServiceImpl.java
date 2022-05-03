package com.hdpros.hdprosbackend.booking.service.implementation;

import com.hdpros.hdprosbackend.booking.dto.BookingDTO;
import com.hdpros.hdprosbackend.booking.dto.BookingDTOResponse;
import com.hdpros.hdprosbackend.booking.model.Booking;
import com.hdpros.hdprosbackend.booking.repository.BookingRepository;
import com.hdpros.hdprosbackend.booking.service.BookingService;
import com.hdpros.hdprosbackend.bvn.service.BvnService;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionResponse;
import com.hdpros.hdprosbackend.places.dto.PlaceDTO;
import com.hdpros.hdprosbackend.places.service.PlaceService;
import com.hdpros.hdprosbackend.room.dto.RoomDTOResponse;
import com.hdpros.hdprosbackend.room.model.Room;
import com.hdpros.hdprosbackend.room.service.RoomService;
import com.hdpros.hdprosbackend.user.dto.ProviderResponse;
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

    private final BvnService bvnService;
    private final RoomService roomService;
    private final PlaceService placeService;
    private final GeneralService generalService;
    private final BookingRepository bookingRepository;
    @Value("${record.count}")
    private int count;

    public BookingServiceImpl(BvnService bvnService, RoomService roomService, PlaceService placeService, GeneralService generalService, BookingRepository bookingRepository) {
        this.bvnService = bvnService;
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

        List<Booking> bookings = bookingRepository.findByUserAndDelFlagOrderByCreatedAtDesc(user, false);

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
    public List<BookingDTOResponse> getBookingForProviderByJobStatus(String email, String statusParam) {
        log.info("Getting bookings for provider");

        if (!generalService.isProvider(email)) {
            throw new GeneralException("User is not provider");
        }

        User user = generalService.getUser(email);

        List<Booking> bookings = getBookingByJobStatusAll(user, statusParam);

        return bookings.stream().map(this::getBookingDTOResponseForProvider).collect(Collectors.toList());
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
        Booking booking;
        Long bookingProviderId = null;
        Long providerId = null;

        //get booking for different users category
        if (!generalService.isProvider(email)) {
            booking = getBooking(user, bookingId);
        } else if (Objects.isNull(user)) {
            booking = getBookingForProvider(bookingId);
        } else {
            booking = getBookingForProvider(bookingId);
            bookingProviderId = booking.getProviderId();
            providerId = user.getId();
        }

        //update job status base on work flow
        if (Objects.equals(statusParam, "accepted")) {
            if (!generalService.isProvider(email) && !booking.isJobStatus()) {
                throw new GeneralException("User not allow to update this status");
            } else {
                booking.setAccepted(true);
                booking.setProviderId(user.getId());
            }
        } else if (Objects.equals(statusParam, "paid")) {
            if (!generalService.isProvider(email) && booking.isAccepted() && Objects.nonNull(user)) {
                booking.setPaid(true);
            } else {
                throw new GeneralException("User not allow to update this status");
            }
        } else if (Objects.equals(statusParam, "in_progress")) {
            if (!generalService.isProvider(email) && !booking.isAccepted() && !booking.isPaid() && Objects.equals(Objects.nonNull(bookingProviderId), Objects.nonNull(providerId))) {
                throw new GeneralException("User not allow to update this status");
            } else {
                booking.setIn_progress(true);
            }
        } else if (Objects.equals(statusParam, "done")) {
            if (!generalService.isProvider(email) && booking.isAccepted() && booking.isPaid() && booking.isIn_progress()) {
                booking.setJobStatus(true);
            } else {
                throw new GeneralException("User not allow to update this status");
            }
        } else if (Objects.equals(statusParam, "processing_payment")) {
            if (booking.isAccepted() && booking.isPaid() && booking.isIn_progress() && booking.isJobStatus() && !Objects.nonNull(user)) {
                booking.setProcessing_payment(true);
            }
        } else if (Objects.equals(statusParam, "completed")) {
            if (booking.isAccepted() && booking.isPaid() && booking.isIn_progress() && booking.isJobStatus() && booking.isProcessing_payment() && !Objects.nonNull(user)) {
                booking.setCompleted(true);
            }
        } else {
            throw new GeneralException("Job status not defined");
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

    @Override
    public BookingDTO getSingleBooking(Long bookingId) {
        log.info("Getting Single booking");

        BookingDTO dto;

        Booking booking = bookingRepository.findByIdAndDelFlag(bookingId, false);

        if (Objects.isNull(booking)) {
            throw new GeneralException("Invalid Request");
        }

        dto = getBookingDTO(booking);

        return dto;
    }

    @Override
    public boolean verifyPayment(VerifyTransactionResponse verifyTransactionResponse, BookingDTO dto) {
        log.info("Updating booking payment info!!!");

        Booking booking = bookingRepository.findByIdAndDelFlag(dto.getId(), false);

        if (Objects.equals(verifyTransactionResponse.getVerifyData().getStatus(), "success")) {

            if (!verifyPaymentRef(verifyTransactionResponse.getVerifyData().getReference())) {
                booking.setPaymentReference(verifyTransactionResponse.getVerifyData().getReference());
                booking.setPaymentDate(LocalDateTime.now());
            } else {
                throw new GeneralException("Payment reference already exist for another booking");
            }
        } else {
            throw new GeneralException("Invalid Request");
        }

        return true;
    }

    @Override
    public boolean verifyPaymentRef(String ref) {
        log.info("Check payment reference");

        if (bookingRepository.existsByPaymentReference(ref)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean verifyTransferRef(String ref) {
        log.info("Check transfer reference");

        if (bookingRepository.existsByTransferReference(ref)) {
            return true;
        }

        return false;
    }

    private Booking getBooking(User user, Long bookingId) {
        log.info("Getting booking for user");

        Booking booking = bookingRepository.findByUserAndIdAndDelFlag(user, bookingId, false);
        if (Objects.isNull(booking)) {
            throw new GeneralException("Invalid Request");
        }
        return booking;
    }

    private Booking getBookingForProvider(Long bookingId) {
        log.info("Getting booking for provider");

        Booking booking = bookingRepository.findByIdAndDelFlag(bookingId, false);
        if (Objects.isNull(booking)) {
            throw new GeneralException("Invalid Request");
        }
        return booking;
    }

    private List<Booking> getBookingByJobStatus(User user, String statusParam) {
        log.info("Getting booking for user by job status");
        List<Booking> booking = null;
        if (Objects.equals(statusParam, "pending")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAcceptedOrderByCreatedAtDesc(user, false, false, false, false);
        } else if (Objects.equals(statusParam, "done")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAcceptedOrderByCreatedAtDesc(user, false, true, true, true);
        } else if (Objects.equals(statusParam, "paid")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAcceptedOrderByCreatedAtDesc(user, false, false, true, true);
        } else if (Objects.equals(statusParam, "accepted")) {
            booking = bookingRepository.findByUserAndDelFlagAndJobStatusAndPaidAndAcceptedOrderByCreatedAtDesc(user, false, false, false, true);
        } else {
            booking = Collections.emptyList();
        }

        if (Objects.isNull(booking)) {
            throw new GeneralException("Invalid Request");
        }

        return booking;
    }

    private List<Booking> getBookingByJobStatusAll(User user, String statusParam) {
        log.info("Getting booking for provider by job status");

        List<Booking> booking = null;
        if (Objects.equals(statusParam, "pending")) {
            booking = bookingRepository.findByJobStatusAndPaidAndAcceptedAndDelFlagOrderByCreatedAtDesc(false, false, false, false);
        } else if (Objects.equals(statusParam, "done")) {
            booking = bookingRepository.findByJobStatusAndPaidAndAcceptedAndDelFlagAndProviderIdOrderByCreatedAtDesc(true, true, true, false, user.getId());
        } else if (Objects.equals(statusParam, "paid")) {
            booking = bookingRepository.findByJobStatusAndPaidAndAcceptedAndDelFlagAndProviderIdOrderByCreatedAtDesc(false, true, true, false, user.getId());
        } else if (Objects.equals(statusParam, "accepted")) {
            booking = bookingRepository.findByJobStatusAndPaidAndAcceptedAndDelFlagAndProviderIdOrderByCreatedAtDesc(false, false, true, false, user.getId());
        } else if (Objects.equals(statusParam, "my_job")) {
            booking = bookingRepository.findByProviderIdAndDelFlagOrderByCreatedAtDesc(user.getId(), false);
        } else {
            booking = Collections.emptyList();
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

        if (Objects.nonNull(booking.getProviderId())) {
            User user = generalService.getUser(booking.getProviderId());

            System.out.println(user.getBvnDetails().getId());
            ProviderResponse response = generalService.getProviderDetail(user);
            response.setBvnDetails(bvnService.getBvnDetailsById(user.getBvnDetails().getId()));

            //set provider detail
            bookingDTOResponse.setProvider(response);
        }

        if (Objects.equals(booking.isCompleted(), true) && Objects.equals(booking.isProcessing_payment(), true) && Objects.equals(booking.isJobStatus(), true) && Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isAccepted(), true) && Objects.equals(booking.isPaid(), true)) {
            bookingDTOResponse.setJobStatus("Completed");
        } else if (Objects.equals(booking.isProcessing_payment(), true) && Objects.equals(booking.isJobStatus(), true) && Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isAccepted(), true) && Objects.equals(booking.isPaid(), true)) {
            bookingDTOResponse.setJobStatus("Processing Payment");
        } else if (Objects.equals(booking.isJobStatus(), true) && Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isAccepted(), true) && Objects.equals(booking.isPaid(), true)) {
            bookingDTOResponse.setJobStatus("Done");
        } else if (Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isPaid(), true) && Objects.equals(booking.isAccepted(), true)) {
            bookingDTOResponse.setJobStatus("In Progress");
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

        return getBookingDTOResponse(booking, bookingDTOResponse, roomsDtoResponses, sum);
    }

    private BookingDTOResponse getBookingDTOResponseForProvider(Booking booking) {
        log.info("Converting Booking to Booking DTO Response for provider");

        BookingDTOResponse bookingDTOResponse = new BookingDTOResponse();
        BeanUtils.copyProperties(booking, bookingDTOResponse);

        if (Objects.nonNull(booking.getProviderId())) {
            User user = generalService.getUser(booking.getProviderId());

            ProviderResponse response = generalService.getProviderDetail(user);

            //set provider detail
            bookingDTOResponse.setProvider(response);
        }

        if (Objects.equals(booking.isCompleted(), true) && Objects.equals(booking.isProcessing_payment(), true) && Objects.equals(booking.isJobStatus(), true) && Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isAccepted(), true) && Objects.equals(booking.isPaid(), true)) {
            bookingDTOResponse.setJobStatus("Completed");
        } else if (Objects.equals(booking.isProcessing_payment(), true) && Objects.equals(booking.isJobStatus(), true) && Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isAccepted(), true) && Objects.equals(booking.isPaid(), true)) {
            bookingDTOResponse.setJobStatus("Processing Payment");
        } else if (Objects.equals(booking.isJobStatus(), true) && Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isAccepted(), true) && Objects.equals(booking.isPaid(), true)) {
            bookingDTOResponse.setJobStatus("Done");
        } else if (Objects.equals(booking.isIn_progress(), true) && Objects.equals(booking.isPaid(), true) && Objects.equals(booking.isAccepted(), true)) {
            bookingDTOResponse.setJobStatus("In Progress");
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

        sum = sum * 0.9;

        return getBookingDTOResponse(booking, bookingDTOResponse, roomsDtoResponses, sum);
    }

    private BookingDTOResponse getBookingDTOResponse(Booking booking, BookingDTOResponse bookingDTOResponse, List<RoomDTOResponse> roomsDtoResponses, double sum) {
        PlaceDTO place = placeService.getPlaceDTO(placeService.getPlace(booking.getUser(), booking.getPlaceId()));

        place.setEmail(booking.getUser().getEmail());

        bookingDTOResponse.setRooms(roomsDtoResponses);
        bookingDTOResponse.setEmail(booking.getUser().getEmail());
        bookingDTOResponse.setPlace(place);
        bookingDTOResponse.setAmount(sum);

        return bookingDTOResponse;
    }
}
