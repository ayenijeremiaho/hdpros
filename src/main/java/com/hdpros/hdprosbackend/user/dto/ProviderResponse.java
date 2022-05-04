package com.hdpros.hdprosbackend.user.dto;

import com.hdpros.hdprosbackend.bvn.model.BvnDetails;
import com.hdpros.hdprosbackend.image.model.Image;
import lombok.Data;

import java.util.List;

@Data
public class ProviderResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String state;

    private String landMark;

    private String address;

    private BvnDetails bvnDetails;

    private Image images;
}
