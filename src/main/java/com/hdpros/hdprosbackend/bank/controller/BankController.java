package com.hdpros.hdprosbackend.bank.controller;

import com.hdpros.hdprosbackend.bank.model.BankDetails;
import com.hdpros.hdprosbackend.bank.service.BankService;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banks")
public class BankController {

    private final BankService bankService;
    private final GeneralService generalService;

    public BankController(BankService bankService, GeneralService generalService) {
        this.bankService = bankService;
        this.generalService = generalService;
    }

    @GetMapping()
    private Response getAllBanks() {
        List<BankDetails> data = bankService.getAllBanks();
        return generalService.prepareSuccessResponse(data);
    }

}
