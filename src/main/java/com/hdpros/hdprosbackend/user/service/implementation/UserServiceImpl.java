package com.hdpros.hdprosbackend.user.service.implementation;

import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.user.Repository.UserRepository;
import com.hdpros.hdprosbackend.user.dto.ProviderResponse;
import com.hdpros.hdprosbackend.user.dto.RegisterUserRequest;
import com.hdpros.hdprosbackend.user.dto.UpdatePasswordRequest;
import com.hdpros.hdprosbackend.user.dto.UserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.user.service.LoginService;
import com.hdpros.hdprosbackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final LoginService loginService;
    private final UserRepository userRepository;

    public UserServiceImpl(@Lazy LoginService loginService, UserRepository userRepository) {
        this.loginService = loginService;
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
    public boolean updatePassword(String email, UpdatePasswordRequest request) {
        User user = getUserByEmail(email);

        user.setChangePassword(true);
        user.setPassword(new BCryptPasswordEncoder().encode(request.getNewPassword()));

        userRepository.save(user);
        return true;
    }

    @Override
    public boolean changePassword(String email, String password) {
        User user = getUserByEmail(email);

        user.setChangePassword(false);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        userRepository.save(user);

        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return Optional.of(userRepository.findByEmailAndDelFlag(email, false))
                .orElseThrow(() -> new GeneralException("Invalid User"));
    }

    @Override
    public UserResponse getUserResponse(User user) {
        log.info("Converting User to User Response for customers");

        UserResponse userResponse = new UserResponse();

        //get avatar
        userResponse.setAvatar(loginService.getProfileImage(user.getId()));

        BeanUtils.copyProperties(user, userResponse);

        return userResponse;
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

    @Override
    public boolean isProvider(String email) {
        return userRepository.existsByEmailAndServiceProvider(email, true);
    }

    @Override
    public User getUserById(Long userId) {
        return Optional.of(userRepository.findByIdAndDelFlag(userId, false))
                .orElseThrow(() -> new GeneralException("Invalid User"));
    }

    @Override
    public ProviderResponse getProviderDTOResponse(User user) {
        log.info("Converting User to Provider DTO Response for provider");

        ProviderResponse providerResponse = new ProviderResponse();
        BeanUtils.copyProperties(user, providerResponse);

        //get avatar
        providerResponse.setAvatar(loginService.getProfileImage(user.getId()));

        return providerResponse;
    }
}
