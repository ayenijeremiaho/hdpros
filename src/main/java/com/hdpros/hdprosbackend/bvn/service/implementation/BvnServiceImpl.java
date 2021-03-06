package com.hdpros.hdprosbackend.bvn.service.implementation;

import com.hdpros.hdprosbackend.bvn.dto.*;
import com.hdpros.hdprosbackend.bvn.model.BvnDetails;
import com.hdpros.hdprosbackend.bvn.repository.BvnDetailsRepository;
import com.hdpros.hdprosbackend.bvn.service.BvnService;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.payment.dto.TransferRecipientRequest;
import com.hdpros.hdprosbackend.payment.dto.TransferRecipientResponse;
import com.hdpros.hdprosbackend.providers.paystack.PaystackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class BvnServiceImpl implements BvnService {

    private final String VERIFICATION_SUCCESSFUL = "Verification Successful";
    private final String VERIFICATION_FAILED = "Verification Failed";

    private final PaystackService paystackService;
    private final BvnDetailsRepository bvnDetailsRepository;

    public BvnServiceImpl(PaystackService paystackService, BvnDetailsRepository bvnDetailsRepository) {
        this.paystackService = paystackService;
        this.bvnDetailsRepository = bvnDetailsRepository;
    }

    @Override
    public BvnCustomerResponse verifyBVN(BvnCustomerRequest request) {
        BvnDetails bvnDetails = bvnDetailsRepository.findByBvnAndAccountNumberAndBankCode(request.getBvn(), request.getBankAccountNumber(), request.getBankCode());

        if (Objects.nonNull(bvnDetails)) {
            return verifiedBVNDetails(bvnDetails);
        } else {
            BvnValidationRequest validationRequest = paystackService.generateValidationRequest(request);
            BvnValidationResponse validationResponse = paystackService.getValidationResponse(validationRequest);

            if (Objects.nonNull(validationResponse)) {
                if ("BVN lookup successful".equals(validationResponse.getMessage())) {
                    bvnDetails = createAndSaveBVNDetails(validationResponse.getBvnData(), request);
                    return verifiedBVNDetails(bvnDetails);
                }
            }
        }
        return failedBVNVerification();
    }

    @Override
    public BvnDetails getBvnDetailsById(Long id) {
        return bvnDetailsRepository.findById(id).orElseThrow(() -> new GeneralException("Invalid BVN Id"));
    }

    //create bvn details in preparation to save
    private BvnDetails createAndSaveBVNDetails(BvnData bvnData, BvnCustomerRequest request) {
        BvnDetails bvnDetails = new BvnDetails();

        boolean isVerified = bvnData.isAccountNumber() && bvnData.isFirstName() && bvnData.isLastName();

        bvnDetails.setBvn(bvnData.getBvn());
        bvnDetails.setAccountNumber(request.getBankAccountNumber());
        bvnDetails.setBankCode(request.getBankCode());
        bvnDetails.setStatus(VERIFICATION_FAILED);
        bvnDetails.setFirstName(request.getFirstName());
        bvnDetails.setLastName(request.getLastName());

        if (isVerified) {
            bvnDetails.setMiddleName(request.getMiddleName());
            bvnDetails.setBlacklisted(bvnData.isBlacklisted());
            bvnDetails.setStatus(VERIFICATION_SUCCESSFUL);

            //create transfer recipient
            TransferRecipientRequest recipientRequest = new TransferRecipientRequest();
            recipientRequest.setAccount_number(request.getBankAccountNumber());
            recipientRequest.setType("nuban");

            String name = request.getFirstName() + " " + request.getLastName();

            recipientRequest.setDescription(name);
            recipientRequest.setName(name);
            recipientRequest.setCurrency("NGN");
            recipientRequest.setBank_code(request.getBankCode());

            log.info("here: " + request.getBankAccountNumber() + " " + request.getBankCode());

            //call service
            TransferRecipientResponse recipientResponse = paystackService.createTransferRecipient(recipientRequest);

            if (Objects.equals(recipientResponse.getMessage(), "Transfer recipient created successfully")) {
                bvnDetails.setRecipientCode(recipientResponse.getTransferRecipientData().getRecipient_code());
            }
        }

        return bvnDetailsRepository.save(bvnDetails);
    }

    //verify the bvn details matches what is being passed in
    private BvnCustomerResponse verifiedBVNDetails(BvnDetails bvnDetails) {
        return BvnCustomerResponse.builder()
                .id(bvnDetails.getId())
                .Status(bvnDetails.getStatus())
                .isBlacklisted(bvnDetails.isBlacklisted())
                .build();
    }

    private BvnCustomerResponse failedBVNVerification() {
        return BvnCustomerResponse.builder()
                .Status(VERIFICATION_FAILED)
                .isBlacklisted(false)
                .build();
    }

}
