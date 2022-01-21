package com.hdpros.hdprosbackend.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterUserRequest {

    @NotNull
    @NotEmpty(message = "Please provide a first name")
    private String firstName;

    @NotNull
    @NotEmpty(message = "Please provide a last name")
    private String lastName;

    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Invalid email")
    private String emailAddress;

    private String phoneNumber;

    @NotNull
    @NotEmpty(message = "Please provide a state")
    private String state;

    @NotNull
    @NotEmpty(message = "Please provide a landmark")
    private String landMark;

    @NotNull
    @NotEmpty(message = "Please provide an address")
    private String address;

    private boolean serviceProvider;

    private Long bvnId;

    private MultipartFile file;

}
