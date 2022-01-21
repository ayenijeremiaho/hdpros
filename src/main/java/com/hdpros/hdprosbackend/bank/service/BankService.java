package com.hdpros.hdprosbackend.bank.service;

import com.hdpros.hdprosbackend.bank.model.BankDetails;

import java.util.List;

public interface BankService {

    void updateBankTable();

    List<BankDetails> getAllBanks();
}
