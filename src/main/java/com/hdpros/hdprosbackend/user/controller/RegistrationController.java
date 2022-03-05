package com.hdpros.hdprosbackend.user.controller;

import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.user.dto.RegisterUserRequest;
import com.hdpros.hdprosbackend.user.dto.RegisterUserResponse;
import com.hdpros.hdprosbackend.user.service.RegistrationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/register")
public class RegistrationController {

    private final GeneralService generalService;
    private final RegistrationService registrationService;

    public RegistrationController(GeneralService generalService, RegistrationService registrationService) {
        this.generalService = generalService;
        this.registrationService = registrationService;
    }

    @PostMapping()
    public Response registerNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        MultipartFile file = registrationService.convertToMultipart(registerUserRequest.getAvatar());

        registerUserRequest.setFile(file);
        RegisterUserResponse response = registrationService.addNewUser(registerUserRequest);

        return generalService.prepareSuccessResponse(response);
    }
}
