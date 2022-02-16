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
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final GeneralService generalService;
    private final LoginService loginService;

    public LoginController(GeneralService generalService, LoginService loginService) {
        this.generalService = generalService;
        this.loginService = loginService;
    }

    @PostMapping()
    public Response getUser(@ApiIgnore Principal principal, @RequestBody LoginUserRequest userRequest) {

        LoginUserResponse userResponse = loginService.loginUser(userRequest);

        return generalService.prepareSuccessResponse(userResponse);

    }
}
