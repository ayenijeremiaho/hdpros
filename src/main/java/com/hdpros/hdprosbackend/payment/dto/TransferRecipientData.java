package com.hdpros.hdprosbackend.payment.dto;

import com.hdpros.hdprosbackend.utils.BaseEntityData;
import lombok.Data;

@Data
public class TransferRecipientData extends BaseEntityData {

    private boolean active;

    private Long id;

    private Long integration;

    private String description;

    private String name;

    private String recipient_code;

    private String type;

}
