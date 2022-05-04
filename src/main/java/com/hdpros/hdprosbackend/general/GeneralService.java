package com.hdpros.hdprosbackend.general;

import com.hdpros.hdprosbackend.user.dto.ProviderResponse;
import com.hdpros.hdprosbackend.user.dto.UserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

import java.math.BigDecimal;
import java.util.HashMap;

public interface GeneralService {
    String getAsString(Object o);

    //used to format the return value of the http post or get request
    String getResponseAsString(HttpResponse<JsonNode> response);

    HashMap<Integer, String> getResponseAsString(HttpResponse<JsonNode> response, boolean getStatus);

    boolean isStringInvalid(String string);

    BigDecimal getAmountAsBigDecimal(String amountString, boolean isKobo);

    //used to format failure response body
    Response prepareFailedResponse(String code, String status, Object message);

    //used to format success response body
    Response prepareSuccessResponse(Object data);

    User getUser(String email);

    boolean isProvider(String email);

    User getUser(Long userId);

    ProviderResponse getProviderDetail(User user);

    UserResponse getUserResponse(User user);
}
