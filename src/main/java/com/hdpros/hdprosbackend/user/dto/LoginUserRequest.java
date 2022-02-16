package com.hdpros.hdprosbackend.user.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginUserRequest {

    @NotNull
    @NotEmpty(message = "Please provide a username")
    private String username;

}
