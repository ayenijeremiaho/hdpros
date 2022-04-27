package com.hdpros.hdprosbackend.providers.paystack;

import com.hdpros.hdprosbackend.bank.dto.BankListResponse;
import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerRequest;
import com.hdpros.hdprosbackend.bvn.dto.BvnValidationRequest;
import com.hdpros.hdprosbackend.bvn.dto.BvnValidationResponse;
import com.hdpros.hdprosbackend.payment.dto.VerifyTransactionResponse;

public interface PaystackService {

    BvnValidationRequest generateValidationRequest(BvnCustomerRequest request);

    BvnValidationResponse getValidationResponse(BvnValidationRequest request);

    BankListResponse getAllBanks(String nextPage);

    VerifyTransactionResponse verifyTransaction(String ref);

}
