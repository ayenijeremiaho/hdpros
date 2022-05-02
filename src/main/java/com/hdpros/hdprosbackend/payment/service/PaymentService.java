package com.hdpros.hdprosbackend.payment.service;

import com.hdpros.hdprosbackend.payment.dto.*;

public interface PaymentService {

    VerifyTransactionResponse verifyTrans(VerifyTransactionRequest verifyTransactionRequest);

    TransferRecipientResponse createTransferRecipient(TransferRecipientRequest recipientRequest);

    TransferResponse transferFund(TransferRequest transferRequest);

}
