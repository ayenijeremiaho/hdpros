package com.hdpros.hdprosbackend.payment.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class VerifyData {

    @SerializedName("id")
    private Long id;

    @SerializedName("booking_id")
    private Long booking_id;

    @SerializedName("status")
    private String status;

    @SerializedName("reference")
    private String reference;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("paid_at")
    private String paid_at;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("channel")
    private String channel;

    @SerializedName("currency")
    private String currency;

    @SerializedName("fees")
    private Double fees;
}
