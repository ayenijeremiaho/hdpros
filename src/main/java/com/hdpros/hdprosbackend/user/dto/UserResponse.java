package com.hdpros.hdprosbackend.user.dto;

import com.hdpros.hdprosbackend.image.model.Image;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String state;

    private String landMark;

    private String address;

    private Image images;

    private String avatar;

}
