package com.hdpros.hdprosbackend.payment.dto;

import lombok.Data;

@Data
public class TransferRequest {

    private String source;

    private String reason;

    private String amount;

    private String recipient;

}
