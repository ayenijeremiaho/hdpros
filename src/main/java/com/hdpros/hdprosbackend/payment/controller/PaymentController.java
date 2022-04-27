package com.hdpros.hdprosbackend.payment.controller;

import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionRequest;
import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionResponse;
import com.hdpros.hdprosbackend.payment.service.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final GeneralService generalService;
    private final PaymentService paymentService;

    public PaymentController(GeneralService generalService, PaymentService paymentService) {
        this.generalService = generalService;
        this.paymentService = paymentService;
    }

    @PostMapping("/verify")
    private Response verifyTransaction(@RequestBody VerifyTransactionRequest request) {

        VerifyTransactionResponse data = paymentService.verifyTrans(request);

        return generalService.prepareSuccessResponse(data);
    }
}
