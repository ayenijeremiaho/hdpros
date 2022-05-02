package com.hdpros.hdprosbackend.payment.dto;

import com.google.gson.annotations.SerializedName;
import com.hdpros.hdprosbackend.bvn.dto.Meta;
import lombok.Data;

@Data
public class TransferRecipientResponse {

    @SerializedName("data")
    private TransferRecipientData transferRecipientData;

    @SerializedName("meta")
    private Meta meta;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

}
