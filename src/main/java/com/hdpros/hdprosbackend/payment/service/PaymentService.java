package com.hdpros.hdprosbackend.payment.service;

import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionRequest;
import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionResponse;

public interface PaymentService {

    VerifyTransactionResponse verifyTrans(VerifyTransactionRequest verifyTransactionRequest);
}
