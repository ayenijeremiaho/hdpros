package com.hdpros.hdprosbackend.bvn.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Meta{

	@SerializedName("free_calls_left")
	private int freeCallsLeft;

	@SerializedName("calls_this_month")
	private int callsThisMonth;
}