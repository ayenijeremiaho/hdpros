package com.hdpros.hdprosbackend.payment.service.Implementation;

import com.hdpros.hdprosbackend.booking.dto.BookingDTO;
import com.hdpros.hdprosbackend.booking.dto.BookingDTOResponse;
import com.hdpros.hdprosbackend.booking.service.BookingService;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.payment.dto.*;
import com.hdpros.hdprosbackend.payment.service.PaymentService;
import com.hdpros.hdprosbackend.providers.paystack.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaystackService paystackService;
    private final BookingService bookingService;
    private final GeneralService generalService;

    public PaymentServiceImpl(PaystackService paystackService, BookingService bookingService, GeneralService generalService) {
        this.paystackService = paystackService;
        this.bookingService = bookingService;
        this.generalService = generalService;
    }

    @Override
    public VerifyTransactionResponse verifyTrans(String userEmail, VerifyTransactionRequest verifyTransactionRequest) {

        VerifyTransactionResponse transactionResponse = new VerifyTransactionResponse();

        if (generalService.isProvider(userEmail)) {
            throw new GeneralException("User can't make payment");
        }

        BookingDTOResponse response = bookingService.getSingleBookingForUser(userEmail, verifyTransactionRequest.getBookingId());

        if (Objects.isNull(response)) {
            throw new GeneralException("Booking does not exist!!!");
        }

        BookingDTO bookingDTO = bookingService.getSingleBooking(verifyTransactionRequest.getBookingId());

        if (Objects.isNull(bookingDTO.getPaymentReference()) || Objects.equals(bookingDTO.getPaymentReference(), verifyTransactionRequest.getTransRef())) {

            transactionResponse = paystackService.verifyTransaction(verifyTransactionRequest.getTransRef());
            if (Objects.equals(transactionResponse.getVerifyData().getStatus(), "success")) {

                if (Objects.isNull(bookingDTO.getPaymentReference())) {
                    if (bookingService.verifyPayment(transactionResponse, bookingDTO)) {
                        bookingService.updateBookingJobStatus(userEmail, verifyTransactionRequest.getBookingId(), "paid");
                    }
                }

            } else {
                throw new GeneralException("Payment verification not successful!!!");
            }
        } else {
            throw new GeneralException("Payment made for booking already");
        }

        return transactionResponse;
    }

    @Override
    public TransferRecipientResponse createTransferRecipient(TransferRecipientRequest recipientRequest) {

        return null;
    }

    @Override
    public TransferResponse transferFund(TransferRequest transferRequest) {
        return null;
    }
}
