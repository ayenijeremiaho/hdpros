package com.hdpros.hdprosbackend.providers.paystack.implementation;

import com.google.gson.Gson;
import com.hdpros.hdprosbackend.bank.dto.BankListResponse;
import com.hdpros.hdprosbackend.bvn.dto.BvnCustomerRequest;
import com.hdpros.hdprosbackend.bvn.dto.BvnValidationRequest;
import com.hdpros.hdprosbackend.bvn.dto.BvnValidationResponse;
import com.hdpros.hdprosbackend.general.ConfigProperty;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.providers.paystack.PaystackService;
import com.hdpros.hdprosbackend.utils.http.HttpService;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PaystackServiceImpl implements PaystackService {

    private final String BANK = "BANK";
    private final String BVN = "BVN";

    private final Gson gson;
    private final HttpService httpService;
    private final ConfigProperty configProperty;
    private final GeneralService generalService;

    public PaystackServiceImpl(Gson gson, HttpService httpService, ConfigProperty configProperty, GeneralService generalService) {
        this.gson = gson;
        this.httpService = httpService;
        this.configProperty = configProperty;
        this.generalService = generalService;
    }

    @Override
    public BvnValidationRequest generateValidationRequest(BvnCustomerRequest request) {
        return BvnValidationRequest.builder()
                .accountNumber(request.getBankAccountNumber())
                .bankCode(request.getBankCode())
                .bvn(request.getBvn())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .build();
    }

    @Override
    public BvnValidationResponse getValidationResponse(BvnValidationRequest request) {
        Map<String, String> headers = getHeaderList();
        String requestBody = generalService.getAsString(request);
        String url = getUrl(BVN);

        HttpResponse<JsonNode> responseObject = httpService.post(headers, requestBody, url);

        String response = generalService.getResponseAsString(responseObject);

        return gson.fromJson(response, BvnValidationResponse.class);
    }

    @Override
    public BankListResponse getAllBanks(String nextPage) {
        Map<String, String> headers = getHeaderList();
        String url = getUrl(BANK);
        Map<String, Object> params = getParams(nextPage);

        HttpResponse<JsonNode> responseObject = httpService.get(headers, params, url);

        String response = generalService.getResponseAsString(responseObject);

        return gson.fromJson(response, BankListResponse.class);
    }

    private String getUrl(String type) {
        String mainUrl = configProperty.getPaystackUrl();
        switch (type) {
            case BANK:
                return mainUrl + configProperty.getPaystackBankUrl();
            case BVN:
                return mainUrl + configProperty.getPaystackBvnUrl();
        }
        throw new GeneralException("Invalid Action");
    }

    private Map<String, Object> getParams(String nextPage){
        Map<String, Object> params = new HashMap<>();
        params.put("country", "nigeria");
        params.put("perPage", 100);

        if(Objects.nonNull(nextPage)) {
            params.put("next", nextPage);
        }

        return params;
    }

    private Map<String, String> getHeaderList() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "Bearer " + configProperty.getPaystackSecret());
        return header;
    }
}
