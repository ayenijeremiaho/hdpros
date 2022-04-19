package com.hdpros.hdprosbackend.user.dto;

import lombok.Data;

@Data
public class LoginUserResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String state;

    private String landMark;

    private String address;

    private boolean serviceProvider;

    private boolean changePassword;

    private String avatar;
}
