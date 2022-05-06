package com.hdpros.hdprosbackend.Scheduler;

import com.hdpros.hdprosbackend.booking.dto.BookingDTOResponse;
import com.hdpros.hdprosbackend.booking.model.Booking;
import com.hdpros.hdprosbackend.booking.service.BookingService;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.payment.dto.ExportTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Scheduler {

    private final GeneralService generalService;
    private final BookingService bookingService;

    public Scheduler(GeneralService generalService, BookingService bookingService) {
        this.generalService = generalService;
        this.bookingService = bookingService;
    }

    @Scheduled(cron = "${scheduled.timeStart}")
    public void sendTransactionReport() {
        log.info("transaction spooling started!!!");

        //get bookings that are already sent for payment processing
        List<Booking> processingPaymentBookings = bookingService.getCompletedBooking("processing_payment");

        //update booking already sent for payment processing to completed
        bookingService.updateSendTransaction(processingPaymentBookings, "completed");

        //get bookings that are completed
        List<Booking> completedBookings = bookingService.getCompletedBooking("done");

        List<ExportTransfer> transferList = completedBookings.stream().map(this::convertBookingToExportTransfer).collect(Collectors.toList());

        LocalDate localDate = LocalDate.now();

        generalService.exportSettlement(transferList, localDate.minusDays(1).toString());
    }

    private ExportTransfer convertBookingToExportTransfer(Booking booking) {
        log.info("convert booking to export transfer dto");

        ExportTransfer exportTransfer = new ExportTransfer();

        BookingDTOResponse response = bookingService.getBookingDTOResponseForProvider(booking);

        exportTransfer.setAmount(response.getAmount());
        exportTransfer.setTransferNote("settlement for customer " + response.getUser().getFirstName() + " with email: " + response.getUser().getEmail());
        exportTransfer.setTransferReference("Booking ID: " + response.getId() + " with description " + response.getDescription());
        exportTransfer.setRecipientCode(response.getProvider().getBvnDetails().getRecipientCode());

        //get other info if recipient code is null
        if (Objects.isNull(response.getProvider().getBvnDetails().getRecipientCode())){
            exportTransfer.setBankCode(response.getProvider().getBvnDetails().getBankCode());
            exportTransfer.setAccountNumber(response.getProvider().getBvnDetails().getAccountNumber());
            exportTransfer.setAccountName(response.getProvider().getBvnDetails().getAccountNumber());
            exportTransfer.setEmail(response.getProvider().getEmail());
        }
        return exportTransfer;
    }
}
