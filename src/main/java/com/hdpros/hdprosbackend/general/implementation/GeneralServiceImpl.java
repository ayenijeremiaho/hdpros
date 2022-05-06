package com.hdpros.hdprosbackend.general.implementation;

import com.google.gson.Gson;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.exceptions.RemoteServiceException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.general.ResponseConstants;
import com.hdpros.hdprosbackend.payment.dto.ExportTransfer;
import com.hdpros.hdprosbackend.user.dto.ProviderResponse;
import com.hdpros.hdprosbackend.user.dto.UserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.user.service.UserService;
import com.hdpros.hdprosbackend.utils.ExportUtil;
import com.hdpros.hdprosbackend.utils.ToExcel;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class GeneralServiceImpl implements GeneralService {

    private final Gson gson;
    private final ExportUtil exportUtil;
    private final UserService userService;
    @Value("${csv.exportBaseName}")
    private String exportBaseName;

    public GeneralServiceImpl(Gson gson, ExportUtil exportUtil, UserService userService) {
        this.gson = gson;
        this.exportUtil = exportUtil;
        this.userService = userService;
    }

    //Used to format object into a string
    @Override
    public String getAsString(Object o) {
        return gson.toJson(o);
    }

    //used to format the return value of the http post or get request
    @Override
    public String getResponseAsString(HttpResponse<JsonNode> response) {
        log.info("getting JSON response as a string");

        if (Objects.nonNull(response)) {
            if (Objects.nonNull(response.getBody())) {
                String body = response.getBody().toPrettyString();
                log.info(body);
                return body;
            }
        }
        throw new RemoteServiceException("No Response from Host");
    }


    @Override
    public HashMap<Integer, String> getResponseAsString(HttpResponse<JsonNode> response, boolean getStatus) {
        log.info("getting JSON response as a Map of body and status");

        HashMap<Integer, String> codeToResponse = new HashMap<>();

        if (Objects.nonNull(response)) {
            String body = response.getBody().toPrettyString();
            log.info(body);
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
        log.info("getting amount as decimal");

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

        log.info("ResponseCode => {}, status => {} and message => {}", code, status, message);

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

        log.info("ResponseCode => {}, status => {} and data => {}", code, status, data);

        return response;
    }

    @Override
    public User getUser(String email) {
        log.info("Getting user details for => {}", email);
        return Optional.of(userService.getUserByEmail(email)).orElseThrow(() -> new GeneralException("Invalid username/email"));
    }

    @Override
    public UserResponse getUserResponse(User user) {
        log.info("Getting user details for => {}", user.getEmail());
        return Optional.of(userService.getUserResponse(user)).orElseThrow(() -> new GeneralException("Invalid username/email"));
    }

    @Override
    public boolean isProvider(String email) {
        log.info("Checking if user is a provider => {}", email);
        return userService.isProvider(email);
    }

    @Override
    public User getUser(Long userId) {
        log.info("Getting user details for => {}", userId);
        return Optional.of(userService.getUserById(userId)).orElseThrow(() -> new GeneralException("Invalid username/email"));
    }

    @Override
    public ProviderResponse getProviderDetail(User user) {
        log.info("Getting provider details for => {}", user.getEmail());
        return Optional.of(userService.getProviderDTOResponse(user)).orElseThrow(() -> new GeneralException("Invalid username/email"));
    }

    @Override
    public void exportSettlement(List<ExportTransfer> transferList, String transactionDay) {
        ToExcel toExcel;
        toExcel = new ToExcel(ExportTransfer.class);
        String filePath = toExcel.getFileName(exportBaseName);
        try {
            //create file directory
            exportUtil.createDirectory("exports");
            String excelPath = toExcel.writeExcel(transferList, "Transfer_Report", "exports/" + filePath);
            //send mail
            if (!excelPath.equals("failed")) {
                log.info("sending mail");
                exportUtil.sendEODMail("HDPros", excelPath, "Transfer Transaction", transactionDay);
            } else {
                log.info("excel creation failed");
            }
        } catch (Exception e) {
            log.info("Error while creating Excel {}", e.getMessage());
        }
    }

}
