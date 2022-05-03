package com.hdpros.hdprosbackend.booking.service;

import com.hdpros.hdprosbackend.booking.dto.BookingDTO;
import com.hdpros.hdprosbackend.booking.dto.BookingDTOResponse;
import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionResponse;

import java.util.List;

public interface BookingService {
    BookingDTOResponse saveBooking(BookingDTO dto);

    BookingDTOResponse updateBooking(BookingDTO dto);

    List<BookingDTOResponse> getBookingForUser(String email);

    List<BookingDTOResponse> getBookingForUserByStatus(String email, String statusParam);

    List<BookingDTOResponse> getBookingForProviderByJobStatus(String email, String statusParam);

    boolean deleteBooking(String email, Long bookingId);

    boolean updateBookingJobStatus(String email, Long bookingId, String statusParam);

    BookingDTOResponse getSingleBookingForUser(String email, Long bookingId);

    BookingDTO getSingleBooking(Long bookingId);

    boolean verifyPayment(VerifyTransactionResponse verifyTransactionResponse, BookingDTO dto);

    boolean verifyPaymentRef(String ref);

    boolean verifyTransferRef(String ref);
}
