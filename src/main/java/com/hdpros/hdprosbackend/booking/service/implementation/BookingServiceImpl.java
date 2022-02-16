package com.hdpros.hdprosbackend.booking.service.implementation;

import com.hdpros.hdprosbackend.booking.dto.BookingDTO;
import com.hdpros.hdprosbackend.booking.model.Booking;
import com.hdpros.hdprosbackend.booking.repository.BookingRepository;
import com.hdpros.hdprosbackend.booking.service.BookingService;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final GeneralService generalService;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(GeneralService generalService, BookingRepository bookingRepository) {
        this.generalService = generalService;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingDTO saveBooking(BookingDTO dto) {
        log.info("Saving booking for user");

        User user = generalService.getUser(dto.getEmail());

        if (bookingRepository.countAllByUserAndDelFlag(user, false) > 5) {
            throw new GeneralException("User can only save upto 5 bookings");
        }

        if (!bookingRepository.existsByDescriptionAndUserAndDelFlag(dto.getDescription(), user, false)) {

            log.info("Saving new booking for user => {}", user.getEmail());
            Booking booking = new Booking();
            BeanUtils.copyProperties(dto, booking);
            booking.setUser(user);

            bookingRepository.save(booking);
            return dto;
        }
        throw new GeneralException("booking with description already created for user");
    }

    @Override
    public BookingDTO updateBooking(BookingDTO dto) {
        log.info("Updating booking for user");

        User user = generalService.getUser(dto.getEmail());

        Booking booking = getBooking(user, dto.getId());

        booking.setJobTitle(dto.getJobTitle());
        booking.setDescription(dto.getDescription());
        booking.setPlaceId(dto.getPlaceId());
        booking.setStartDate(dto.getStartDate());
        booking.setStartTime(dto.getStartTime());

        Booking updateBooking = bookingRepository.save(booking);
        return getBookingDTO(updateBooking);
    }


    @Override
    public List<BookingDTO> getBookingForUser(String email) {
        log.info("Getting bookings for user");

        User user = generalService.getUser(email);

        List<Booking> bookings = bookingRepository.findByUserAndDelFlag(user, false);

        return bookings.stream().map(this::getBookingDTO).collect(Collectors.toList());
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

    private Booking getBooking(User user, Long bookingId) {
        log.info("Getting Room for user");

        Booking booking = bookingRepository.findByUserAndIdAndDelFlag(user, bookingId, false);
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
}
