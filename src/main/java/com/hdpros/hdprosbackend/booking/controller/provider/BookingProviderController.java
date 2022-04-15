package com.hdpros.hdprosbackend.booking.controller.provider;

import com.hdpros.hdprosbackend.booking.dto.BookingDTOResponse;
import com.hdpros.hdprosbackend.booking.service.BookingService;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings/provider")
public class BookingProviderController {

    private final BookingService bookingService;
    private final GeneralService generalService;

    public BookingProviderController(BookingService bookingService, GeneralService generalService) {
        this.bookingService = bookingService;
        this.generalService = generalService;
    }

    @PostMapping("/jobStatus")
    public Response getBookingByJobStatus(@ApiIgnore Principal principal, @RequestParam(name = "status", defaultValue = "pending") String status) {

        List<BookingDTOResponse> response = bookingService.getBookingForProviderByJobStatus(principal.getName(), status);

        return generalService.prepareSuccessResponse(response);
    }

    @PostMapping("/jobStatus/{BookingId}")
    public Response updateBookingJobStatus(@ApiIgnore Principal principal, @PathVariable Long BookingId, @RequestParam(name = "status", defaultValue = "pending") String status) {

        boolean response = bookingService.updateBookingJobStatus(principal.getName(), BookingId, status);

        return generalService.prepareSuccessResponse(response);
    }
}
