package com.hdpros.hdprosbackend.payment.dto;

import lombok.Data;

@Data
public class ExportTransfer {

    private Double transferAmount;

    private String transferNote;

    private String transferReference;

    private String recipientCode;

    private String bankCodeOrSlug;

    private String accountNumber;

    private String accountName;

    private String emailAddress;

}
