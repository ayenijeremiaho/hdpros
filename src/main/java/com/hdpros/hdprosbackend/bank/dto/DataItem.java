package com.hdpros.hdprosbackend.bank.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DataItem{

	@SerializedName("country")
	private String country;

	@SerializedName("code")
	private String code;

	@SerializedName("pay_with_bank")
	private boolean payWithBank;

	@SerializedName("longcode")
	private String longcode;

	@SerializedName("active")
	private boolean active;

	@SerializedName("type")
	private String type;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("is_deleted")
	private boolean isDeleted;

	@SerializedName("name")
	private String name;

	@SerializedName("currency")
	private String currency;

	@SerializedName("id")
	private int id;

	@SerializedName("slug")
	private String slug;

	@SerializedName("gateway")
	private Object gateway;

	@SerializedName("updatedAt")
	private String updatedAt;
}