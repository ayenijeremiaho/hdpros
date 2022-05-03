package com.hdpros.hdprosbackend.payment.dto;

import lombok.Data;

@Data
public class ExportTransfer {

    private Double amount;

    private String transferNote;

    private String transferReference;

    private String recipientCode;

    private String bankCode;

    private String accountNumber;

    private String accountName;

    private String email;

}
