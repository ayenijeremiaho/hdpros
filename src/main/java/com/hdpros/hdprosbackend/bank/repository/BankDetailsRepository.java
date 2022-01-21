package com.hdpros.hdprosbackend.bank.repository;

import com.hdpros.hdprosbackend.bank.model.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {
}
