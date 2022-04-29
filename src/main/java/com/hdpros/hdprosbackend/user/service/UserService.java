package com.hdpros.hdprosbackend.user.service;

import com.hdpros.hdprosbackend.user.dto.ProviderResponse;
import com.hdpros.hdprosbackend.user.dto.UpdatePasswordRequest;
import com.hdpros.hdprosbackend.user.model.User;

public interface UserService {
    boolean updatePassword(String email, UpdatePasswordRequest request);

    User getUserByEmail(String email);

    User saveUser(User user);

    boolean userExistByEmail(String email);

    boolean isProvider(String email);

    boolean changePassword(String email, String password);

    User getUserById(Long userId);

    ProviderResponse getProviderDTOResponse(User user);
}
