package com.hdpros.hdprosbackend.user.controller;

import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.user.dto.UpdatePasswordRequest;
import com.hdpros.hdprosbackend.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/updatePassword")
public class UpdatePasswordController {

    private final UserService userService;
    private final GeneralService generalService;

    public UpdatePasswordController(UserService userService, GeneralService generalService) {
        this.userService = userService;
        this.generalService = generalService;
    }

    @PostMapping()
    public Response updatePassword(@ApiIgnore Principal principal, @RequestBody UpdatePasswordRequest userRequest) {
        //update the email of the user to that of the logged-in user
        String email = principal.getName();

        boolean status = userService.updatePassword(email, userRequest);

        return generalService.prepareSuccessResponse(status);

    }

}
