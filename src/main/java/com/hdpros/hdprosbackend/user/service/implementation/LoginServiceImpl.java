package com.hdpros.hdprosbackend.user.service.implementation;

import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.image.service.ImageService;
import com.hdpros.hdprosbackend.user.dto.LoginUserRequest;
import com.hdpros.hdprosbackend.user.dto.LoginUserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.user.service.LoginService;
import com.hdpros.hdprosbackend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private final UserService userService;
    private final ImageService imageService;


    public LoginServiceImpl(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @Override
    public LoginUserResponse loginUser(LoginUserRequest request) {
        log.info("Getting user info");

        //verify if email exist
        User user = userService.getUserByEmail(request.getUsername());

        if (Objects.isNull(user)) {
            throw new GeneralException("User does not exist!!!");
        }

        LoginUserResponse userResponse = new LoginUserResponse();
        BeanUtils.copyProperties(user, userResponse);

        //get profile image
        String image = getProfileImage(user.getId());
        userResponse.setAvatar(image);

        return userResponse;
    }

    private String getProfileImage(Long userId) {
        Image image = imageService.getProfileImage(userId);

        return Objects.nonNull(image) ? image.getUrl() : null;
    }
}
