package com.hdpros.hdprosbackend.bvn.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BvnCustomerResponse {
    private Long id;
    private String Status;
    private boolean isBlacklisted;
}
