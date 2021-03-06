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

        if ((long) transferList.size() > 0) {
            if (generalService.exportSettlement(transferList, localDate.minusDays(1).toString())) {

                //update booking just sent for payment processing
                bookingService.updateSendTransaction(completedBookings, "processing_payment");

            }
        }

    }

    private ExportTransfer convertBookingToExportTransfer(Booking booking) {
        log.info("convert booking to export transfer dto");

        ExportTransfer exportTransfer = new ExportTransfer();

        BookingDTOResponse response = bookingService.getBookingDTOResponseForProvider(booking);

        exportTransfer.setTransferAmount(response.getAmount());
        exportTransfer.setTransferNote("settlement for customer " + response.getUser().getFirstName() + " with email: " + response.getUser().getEmail() + " booking id " + response.getId());
        exportTransfer.setTransferReference(response.getProvider().getBvnDetails().getRecipientCode()+"_"+response.getId());
        exportTransfer.setRecipientCode(response.getProvider().getBvnDetails().getRecipientCode());
        exportTransfer.setEmailAddress(response.getProvider().getEmail());

        //get other info if recipient code is null
        if (Objects.isNull(response.getProvider().getBvnDetails().getRecipientCode())) {
            exportTransfer.setBankCodeOrSlug(response.getProvider().getBvnDetails().getBankCode());
            exportTransfer.setAccountNumber(response.getProvider().getBvnDetails().getAccountNumber());

            //get firstname & lastname
            String firstname = response.getProvider().getBvnDetails().getFirstName();
            String lastname = response.getProvider().getBvnDetails().getLastName();

            exportTransfer.setAccountName(firstname + " " + lastname);
        }
        return exportTransfer;
    }
}
