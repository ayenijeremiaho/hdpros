package com.hdpros.hdprosbackend.user.service.implementation;

import com.hdpros.hdprosbackend.bvn.model.BvnDetails;
import com.hdpros.hdprosbackend.bvn.service.BvnService;
import com.hdpros.hdprosbackend.email.service.MailService;
import com.hdpros.hdprosbackend.exceptions.GeneralException;
import com.hdpros.hdprosbackend.image.model.Image;
import com.hdpros.hdprosbackend.image.service.ImageService;
import com.hdpros.hdprosbackend.providers.cloudinary.CloudinaryService;
import com.hdpros.hdprosbackend.user.dto.RegisterUserRequest;
import com.hdpros.hdprosbackend.user.dto.RegisterUserResponse;
import com.hdpros.hdprosbackend.user.model.User;
import com.hdpros.hdprosbackend.user.service.RegistrationService;
import com.hdpros.hdprosbackend.user.service.UserService;
import com.hdpros.hdprosbackend.utils.GeneralUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${mailing-list}")
    private List<String> mailingList;

    private final BvnService bvnService;
    private final MailService mailService;
    private final UserService userService;
    private final ImageService imageService;
    private final CloudinaryService cloudinaryService;

    public RegistrationServiceImpl(BvnService bvnService, MailService mailService, UserService userService, ImageService imageService, CloudinaryService cloudinaryService) {
        this.bvnService = bvnService;
        this.mailService = mailService;
        this.userService = userService;
        this.imageService = imageService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public RegisterUserResponse addNewUser(RegisterUserRequest request) {

        //verify if email already exist
        if (userService.userExistByEmail(request.getEmail())) {
            throw new GeneralException("Email already exist, Login or reset password");
        }

        //create user object and map registration request to user object
        User user = new User();
        BeanUtils.copyProperties(request, user);

        //confirm if provider has provided bvn
        if (request.isServiceProvider()) {
            if (Objects.isNull(request.getBvnId())) {
                throw new GeneralException("Service provider must have valid BVN");
            }
            BvnDetails bvnDetails = bvnService.getBvnDetailsById(request.getBvnId());

            //add bvn details to user object
            user.setBvnDetails(bvnDetails);
        }

        //generate and set password
        String password = GeneralUtil.generateRandomWord(10);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        user = userService.saveUser(user);

        //verify image was uploaded and upload image
        if (Objects.nonNull(request.getFile())) {
            Map<String, String> imageMap = cloudinaryService.upload(request.getFile());

            if (Objects.nonNull(imageMap)) {
                String publicId = imageMap.get("publicId");
                String imageUrl = imageMap.get("url");

                //save profile image
                Image image = imageService.saveProfileImage(publicId, imageUrl, user);
                user.setImages(image);
                userService.saveUser(user);
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userName", user.getEmail());
        map.put("name", user.getFirstName() + " " + user.getLastName());
        map.put("password", password);

        if (request.isServiceProvider()) {
            map.put("userType", "Service Provider");
        } else {
            map.put("userType", "Customer");
        }

        //send mail to user
        String[] copy = mailingList.toArray(new String[0]);

        mailService.sendMail("New User Registration",
                user.getEmail(), copy, map, "new_user");

        return RegisterUserResponse.builder().status("SUCCESSFUL").build();
    }
}
