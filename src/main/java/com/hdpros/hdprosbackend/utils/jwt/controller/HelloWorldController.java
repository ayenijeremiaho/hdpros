package com.hdpros.hdprosbackend.utils.jwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@CrossOrigin()
public class HelloWorldController {

    private final Authentication authentication;

    public HelloWorldController(Authentication authentication) {
        this.authentication = authentication;
    }

    @RequestMapping({"/hello"})
    public String hello(Principal principal) {
        String message = "Logged in user => {}" + principal.getName();
        log.info(message);
        return message;
    }

}
