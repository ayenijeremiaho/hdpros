package com.hdpros.hdprosbackend.bvn.repository;

import com.hdpros.hdprosbackend.bvn.model.BvnDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BvnDetailsRepository extends JpaRepository<BvnDetails, Long> {
    BvnDetails findByBvnAndAccountNumberAndBankCode(String bvn, String accountNumber, String bankCode);
}
