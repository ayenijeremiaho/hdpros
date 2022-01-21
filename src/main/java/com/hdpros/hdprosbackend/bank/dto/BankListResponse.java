package com.hdpros.hdprosbackend.bank.dto;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BankListResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("meta")
	private Meta meta;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;
}