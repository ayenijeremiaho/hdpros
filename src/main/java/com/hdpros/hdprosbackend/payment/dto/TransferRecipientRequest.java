package com.hdpros.hdprosbackend.payment.dto;

import lombok.Data;

@Data
public class TransferRecipientRequest {

    private String description;

    private String account_number;

    private String bank_code;

    private String currency;

}
