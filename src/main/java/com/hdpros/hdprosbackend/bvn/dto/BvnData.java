package com.hdpros.hdprosbackend.bvn.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BvnData {

	@SerializedName("account_number")
	private boolean accountNumber;

	@SerializedName("is_blacklisted")
	private boolean isBlacklisted;

	@SerializedName("last_name")
	private boolean lastName;

	@SerializedName("bvn")
	private String bvn;

	@SerializedName("middle_name")
	private boolean middleName;

	@SerializedName("first_name")
	private boolean firstName;
}