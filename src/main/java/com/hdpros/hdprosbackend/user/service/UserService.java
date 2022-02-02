package com.hdpros.hdprosbackend.user.service;

import com.hdpros.hdprosbackend.user.model.User;

public interface UserService {
    User saveUser(User user);

    boolean userExistByEmail(String email);
}
