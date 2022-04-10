package com.hdpros.hdprosbackend.user.controller;

import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.user.dto.LoginUserRequest;
import com.hdpros.hdprosbackend.user.dto.LoginUserResponse;
import com.hdpros.hdprosbackend.user.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private final GeneralService generalService;
    private final LoginService loginService;

    public LoginController(GeneralService generalService, LoginService loginService) {
        this.generalService = generalService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public Response getUser(@RequestBody LoginUserRequest userRequest) {

        LoginUserResponse userResponse = loginService.loginUser(userRequest);

        return generalService.prepareSuccessResponse(userResponse);

    }

    @PostMapping("/resetPassword")
    public Response resetPassword(@RequestBody LoginUserRequest userRequest) {

        boolean status = loginService.resetPassword(userRequest);

        return generalService.prepareSuccessResponse(status);

    }

}
