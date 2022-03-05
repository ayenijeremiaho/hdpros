package com.hdpros.hdprosbackend.user.service;

import com.hdpros.hdprosbackend.user.dto.RegisterUserRequest;
import com.hdpros.hdprosbackend.user.dto.RegisterUserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface RegistrationService {

    MultipartFile convertToMultipart(String base64);

    RegisterUserResponse addNewUser(RegisterUserRequest request);
}
