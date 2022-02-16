package com.hdpros.hdprosbackend.booking.service;

import com.hdpros.hdprosbackend.booking.dto.BookingDTO;

import java.util.List;

public interface BookingService {
    BookingDTO saveBooking(BookingDTO dto);

    BookingDTO updateBooking(BookingDTO dto);

    List<BookingDTO> getBookingForUser(String email);

    boolean deleteBooking(String email, Long bookingId);
}
