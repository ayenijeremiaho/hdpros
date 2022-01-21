package com.hdpros.hdprosbackend.user.controller;

import com.hdpros.hdprosbackend.general.Response;
import com.hdpros.hdprosbackend.user.dto.RegisterUserRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/register")
public class RegistrationController {


    @PostMapping()
    public Response registerNewUser(@Valid @RequestBody RegisterUserRequest registerUserRequest, @RequestParam(value = "avatar", required = false) MultipartFile file) {

        return new Response();

    }
}
