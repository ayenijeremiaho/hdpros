package com.hdpros.hdprosbackend.user.service.implementation;

import com.hdpros.hdprosbackend.email.service.MailService;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.image.service.ImageService;
import com.hdpros.hdprosbackend.user.dto.LoginUserRequest;
import com.hdpros.hdprosbackend.user.dto.LoginUserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.user.service.LoginService;
import com.hdpros.hdprosbackend.user.service.UserService;
import com.hdpros.hdprosbackend.utils.GeneralUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Value("${mailing-list}")
    private List<String> mailingList;

    private final MailService mailService;
    private final UserService userService;
    private final ImageService imageService;

    public LoginServiceImpl(MailService mailService, UserService userService, ImageService imageService) {
        this.mailService = mailService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @Override
    public LoginUserResponse loginUser(LoginUserRequest request) {
        User user = getUser(request);

        LoginUserResponse userResponse = new LoginUserResponse();
        BeanUtils.copyProperties(user, userResponse);

        //get profile image
        String image = getProfileImage(user.getId());
        userResponse.setAvatar(image);

        return userResponse;
    }


    @Override
    public boolean resetPassword(LoginUserRequest request) {
        User user = getUser(request);

        String password = GeneralUtil.generateRandomWord(6);
        user.setChangePassword(true);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getEmail());
        map.put("password", password);

        //send mail to user
        String[] copy = mailingList.toArray(new String[0]);

        mailService.sendMail("Reset Password", user.getEmail(), copy, map, "reset_password");

        return true;
    }

    private User getUser(LoginUserRequest request) {
        log.info("Getting user info");

        //verify if email exist
        User user = userService.getUserByEmail(request.getUsername());

        if (Objects.isNull(user)) {
            throw new GeneralException("User does not exist!!!");
        }
        return user;
    }

    private String getProfileImage(Long userId) {
        Image image = imageService.getProfileImage(userId);

        return Objects.nonNull(image) ? image.getUrl() : null;
    }
}
