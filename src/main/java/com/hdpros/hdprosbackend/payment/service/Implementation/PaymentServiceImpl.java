package com.hdpros.hdprosbackend.payment.service.Implementation;

import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionRequest;
import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionResponse;
import com.hdpros.hdprosbackend.payment.service.PaymentService;
import com.hdpros.hdprosbackend.providers.paystack.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaystackService paystackService;

    public PaymentServiceImpl(PaystackService paystackService) {
        this.paystackService = paystackService;
    }

    @Override
    public VerifyTransactionResponse verifyTrans(VerifyTransactionRequest verifyTransactionRequest) {

        return paystackService.verifyTransaction(verifyTransactionRequest.getTransRef());
    }

}
