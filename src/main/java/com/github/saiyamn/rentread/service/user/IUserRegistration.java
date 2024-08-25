package com.github.saiyamn.rentread.service.user;

import com.github.saiyamn.rentread.dto.UserRegistrationRequestBody;
import com.github.saiyamn.rentread.entity.User;

public interface IUserRegistration {

    public User registerUser(UserRegistrationRequestBody userRegistrationRequestBody);
}
