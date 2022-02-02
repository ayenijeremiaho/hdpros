package com.hdpros.hdprosbackend.bvn.service;

import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerRequest;
import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerResponse;
import com.hdpros.hdprosbackend.bvn.model.BvnDetails;

public interface BvnService {

    //for BVN Validation
    BvnCustomerResponse verifyBVN(BvnCustomerRequest request);

    //get BVN by ID
    BvnDetails getBvnDetailsById(Long id);
}
