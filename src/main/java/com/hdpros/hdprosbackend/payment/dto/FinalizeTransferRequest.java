package com.hdpros.hdprosbackend.payment.dto;

import lombok.Data;

@Data
public class FinalizeTransferRequest {

    private String transfer_code;

    private Long otp;

}
