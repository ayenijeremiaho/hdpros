package com.hdpros.hdprosbackend.utils.jwt.service;

import com.hdpros.hdprosbackend.user.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.hdpros.hdprosbackend.user.model.User user = userService.getUserByEmail(username);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

}