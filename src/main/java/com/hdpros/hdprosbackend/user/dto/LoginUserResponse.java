package com.hdpros.hdprosbackend.user.dto;

import com.hdpros.hdprosbackend.user.model.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginUserResponse {

    private User user;

}
