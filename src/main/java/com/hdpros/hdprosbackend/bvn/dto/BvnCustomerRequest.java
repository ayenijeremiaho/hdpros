package com.hdpros.hdprosbackend.bvn.dto;

import lombok.Data;

@Data
public class BvnCustomerRequest {

    private String bvn;

    private String bankCode;

    private String bankAccountNumber;

    private String firstName;

    private String lastName;

    private String middleName;
}
