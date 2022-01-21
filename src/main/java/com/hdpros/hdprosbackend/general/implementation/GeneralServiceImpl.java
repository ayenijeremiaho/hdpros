package com.hdpros.hdprosbackend.general.implementation;

import com.google.gson.Gson;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.exceptions.RemoteServiceException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.general.ResponseConstants;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Objects;

@Service
public class GeneralServiceImpl implements GeneralService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Gson gson;

    public GeneralServiceImpl(Gson gson) {
        this.gson = gson;
    }

    //Used to format object into a string
    @Override
    public String getAsString(Object o) {
        return gson.toJson(o);
    }

    //used to format the return value of the http post or get request
    @Override
    public String getResponseAsString(HttpResponse<JsonNode> response) {
        logger.info("getting JSON response as a string");

        if (Objects.nonNull(response)) {
            if (Objects.nonNull(response.getBody())) {
                String body = response.getBody().toPrettyString();
                logger.info(body);
                return body;
            }
        }
        throw new RemoteServiceException("No Response from Host");
    }


    @Override
    public HashMap<Integer, String> getResponseAsString(HttpResponse<JsonNode> response, boolean getStatus) {
        logger.info("getting JSON response as a Map of body and status");

        HashMap<Integer, String> codeToResponse = new HashMap<>();

        if (Objects.nonNull(response)) {
            String body = response.getBody().toPrettyString();
            logger.info(body);
            codeToResponse.put(response.getStatus(), body);
            return codeToResponse;
        }
        throw new RemoteServiceException("No Response from Host");
    }


    @Override
    public boolean isStringInvalid(String string) {
        return Objects.isNull(string) || string.trim().equals("");
    }


    @Override
    public BigDecimal getAmountAsBigDecimal(String amountString, boolean isKobo) {
        logger.info("getting amount as decimal");

        BigDecimal amount;

        if (Objects.isNull(amountString)) {
            throw new GeneralException("Invalid Amount");
        }

        if (amountString.equals("") || amountString.equals("0")) {
            throw new GeneralException("Invalid Amount");
        } else {
            amount = new BigDecimal(amountString);
        }

        if (isKobo) {
            return amount.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        } else {
            return amount;
        }
    }


    //used to format failed response body
    @Override
    public Response prepareFailedResponse(String code, String status, Object message) {
        Response response = new Response();
        response.setResponseCode(code);
        response.setStatus(status);
        response.setFailureReason(message);

        logger.info("ResponseCode => {}, status => {} and message => {}", code, status, message);

        return response;
    }

    @Override
    public Response prepareSuccessResponse(Object data) {
        Response response = new Response();

        String code = ResponseConstants.ResponseCode.SUCCESS;
        String status = ResponseConstants.ResponseMessage.SUCCESS;

        response.setResponseCode(code);
        response.setStatus(status);
        response.setData(data);

        logger.info("ResponseCode => {}, status => {} and data => {}", code, status, data);

        return response;
    }

}
