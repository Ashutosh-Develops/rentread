package com.github.saiyamn.rentread.controller;

import com.github.saiyamn.rentread.dto.UserRegistrationRequestBody;
import com.github.saiyamn.rentread.dto.UserRegistrationResponseBody;
import com.github.saiyamn.rentread.entity.User;
import com.github.saiyamn.rentread.service.user.IUserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    IUserRegistration iUserRegistration;

    @PostMapping(value = "/register")
    public UserRegistrationResponseBody registerUser(@RequestBody UserRegistrationRequestBody userRegistrationRequestBody){
        User user=iUserRegistration.registerUser(userRegistrationRequestBody);
        UserRegistrationResponseBody responseBody=new UserRegistrationResponseBody(user);
        return responseBody;

    }
}
