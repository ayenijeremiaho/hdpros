package com.hdpros.hdprosbackend.payment.service;

import com.hdpros.hdprosbackend.payment.dto.*;

public interface PaymentService {

    VerifyTransactionResponse verifyTrans(String userEmail, VerifyTransactionRequest verifyTransactionRequest);

    TransferRecipientResponse createTransferRecipient(TransferRecipientRequest recipientRequest);

    TransferResponse transferFund(TransferRequest transferRequest);

}
