package com.hdpros.hdprosbackend.payment.dto;

import com.hdpros.hdprosbackend.utils.BaseEntityData;
import lombok.Data;

@Data
public class TransferResponseData extends BaseEntityData {

    private Double amount;

    private String reference;

    private String reason;

    private String status;

    private String transfer_code;

    private Long id;

    private Long integration;

    private Long request;

    private Long recipient;

}
