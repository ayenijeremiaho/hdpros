package com.hdpros.hdprosbackend.bank.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Meta{

	@SerializedName("next")
	private String next;

	@SerializedName("perPage")
	private int perPage;

	@SerializedName("previous")
	private Object previous;
}