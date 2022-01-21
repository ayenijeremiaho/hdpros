package com.hdpros.hdprosbackend.bvn.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BvnValidationResponse {

	@SerializedName("data")
	private BvnData bvnData;

	@SerializedName("meta")
	private Meta meta;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;
}