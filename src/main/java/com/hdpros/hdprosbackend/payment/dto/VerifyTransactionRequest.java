package com.hdpros.hdprosbackend.payment.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyTransactionRequest {

    @SerializedName("transaction_reference")
    private String transRef;

    @SerializedName("booking_id")
    private Long bookingId;

}
