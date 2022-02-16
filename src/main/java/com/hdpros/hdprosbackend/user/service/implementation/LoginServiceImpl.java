package com.hdpros.hdprosbackend.user.service.implementation;

import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.general.GeneralService;
import com.hdpros.hdprosbackend.user.service.LoginService;
import com.hdpros.hdprosbackend.user.service.UserService;
import com.hdpros.hdprosbackend.user.dto.LoginUserRequest;
import com.hdpros.hdprosbackend.user.dto.LoginUserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private final GeneralService generalService;
    private final UserService userService;

    public LoginServiceImpl(GeneralService generalService, UserService userService) {
        this.generalService = generalService;
        this.userService = userService;
    }

    @Override
    public LoginUserResponse loginUser(LoginUserRequest request) {

        User user = new User();

        //verify if email exist
        if (userService.userExistByEmail(request.getUsername())) {
            log.info("Getting user info");

            user = userService.getUserByEmail(request.getUsername());

        } else {
            throw new GeneralException("User does not exist!!!");
        }

        return LoginUserResponse.builder().user(user).build();
    }

}
