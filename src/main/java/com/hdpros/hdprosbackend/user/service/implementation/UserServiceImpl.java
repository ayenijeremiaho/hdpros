package com.hdpros.hdprosbackend.user.service.implementation;

import com.hdpros.hdprosbackend.user.Repository.UserRepository;
import com.hdpros.hdprosbackend.user.dto.RegisterUserRequest;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private User saveUser(RegisterUserRequest userInfo) {
        User user = new User();

        user.setAddress(userInfo.getAddress());
        user.setEmail(userInfo.getEmail());
        user.setFirstName(userInfo.getFirstName());
        user.setLandMark(userInfo.getLandMark());
        user.setLastName(userInfo.getLastName());
        user.setState(userInfo.getState());
        user.setPhoneNumber(userInfo.getPhoneNumber());

        return user;

    }

    @Override
    public User saveUser(User user) {
        log.info("Saving user info for {}", user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
