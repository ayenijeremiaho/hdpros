package com.hdpros.hdprosbackend.user.service;

import com.hdpros.hdprosbackend.user.dto.LoginUserRequest;
import com.hdpros.hdprosbackend.user.dto.LoginUserResponse;

public interface LoginService {

    LoginUserResponse loginUser(LoginUserRequest request);

}
