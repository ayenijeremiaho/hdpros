package com.hdpros.hdprosbackend.bvn.controller;

import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerRequest;
import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerResponse;
import com.hdpros.hdprosbackend.bvn.service.BvnService;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bvn")
public class BvnController {

    private final BvnService bvnService;
    private final GeneralService generalService;

    public BvnController(BvnService bvnService, GeneralService generalService) {
        this.bvnService = bvnService;
        this.generalService = generalService;
    }

    @PostMapping("/validate")
    private Response validateResponse(@RequestBody BvnCustomerRequest request) {
        BvnCustomerResponse data = bvnService.verifyBVN(request);
        return generalService.prepareSuccessResponse(data);
    }
}
