package com.hdpros.hdprosbackend.user.service;

import com.hdpros.hdprosbackend.user.dto.RegisterUserRequest;
import com.hdpros.hdprosbackend.user.dto.RegisterUserResponse;

public interface RegistrationService {

    RegisterUserResponse addNewUser(RegisterUserRequest request);
}
