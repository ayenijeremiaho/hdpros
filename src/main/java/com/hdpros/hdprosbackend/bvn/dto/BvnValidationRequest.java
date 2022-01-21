package com.hdpros.hdprosbackend.bvn.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BvnValidationRequest{

	@SerializedName("account_number")
	private String accountNumber;

	@SerializedName("bank_code")
	private String bankCode;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("bvn")
	private String bvn;

	@SerializedName("middle_name")
	private String middleName;

	@SerializedName("first_name")
	private String firstName;
}