package com.hdpros.hdprosbackend.booking.controller;

import com.hdpros.hdprosbackend.booking.dto.BookingDTO;
import com.hdpros.hdprosbackend.booking.dto.BookingDTOResponse;
import com.hdpros.hdprosbackend.booking.service.BookingService;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final GeneralService generalService;

    public BookingController(BookingService bookingService, GeneralService generalService) {
        this.bookingService = bookingService;
        this.generalService = generalService;
    }

    @PostMapping("/add")
    public Response addBooking(@ApiIgnore Principal principal, @RequestBody BookingDTO bookingDTO) {
        //update the email of the user to that of the logged-in user
        bookingDTO.setEmail(principal.getName());

        BookingDTOResponse response = bookingService.saveBooking(bookingDTO);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/update")
    public Response updateBooking(@ApiIgnore Principal principal, @RequestBody BookingDTO bookingDTO) {
        //update the email of the user to that of the logged-in user
        bookingDTO.setEmail(principal.getName());

        BookingDTOResponse response = bookingService.updateBooking(bookingDTO);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/delete/{BookingId}")
    public Response deleteBooking(@ApiIgnore Principal principal, @PathVariable Long BookingId) {
        boolean response = bookingService.deleteBooking(principal.getName(), BookingId);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/all")
    public Response allBookings(@ApiIgnore Principal principal) {
        List<BookingDTOResponse> response = bookingService.getBookingForUser(principal.getName());

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/{bookingId}")
    public Response getSingleBooking(@ApiIgnore Principal principal, @PathVariable Long bookingId) {
        BookingDTOResponse response = bookingService.getSingleBookingForUser(principal.getName(), bookingId);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/jobStatus/{BookingId}")
    public Response updateBookingJobStatus(@ApiIgnore Principal principal, @PathVariable Long BookingId, @RequestParam(name = "status", defaultValue = "pending") String status) {
        boolean response = bookingService.updateBookingJobStatus(principal.getName(), BookingId, status);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/jobStatus")
    public Response getBookingByJobStatus(@ApiIgnore Principal principal, @RequestParam(name = "status", defaultValue = "pending") String status) {

        List<BookingDTOResponse> response = bookingService.getBookingForUserByStatus(principal.getName(), status);

        return generalService.prepareSuccessResponse(response);
    }

}
