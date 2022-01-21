package com.hdpros.hdprosbackend.bvn.service;

import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerRequest;
import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerResponse;

public interface BvnService {

    //for BVN Validation
    BvnCustomerResponse verifyBVN(BvnCustomerRequest request);
}
