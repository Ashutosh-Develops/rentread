package com.github.saiyamn.rentread.service.user;

import com.github.saiyamn.rentread.dto.UserRegistrationRequestBody;
import com.github.saiyamn.rentread.entity.Role;
import com.github.saiyamn.rentread.entity.User;
import com.github.saiyamn.rentread.exception.InvalidInputException;
import com.github.saiyamn.rentread.repository.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements IUserRegistration{

    @Autowired
    IUserRepository iUserRepository;

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Override
    public User registerUser(UserRegistrationRequestBody userRegistrationRequestBody) {

       String firstName = userRegistrationRequestBody.getFirstName();
       String lastName = userRegistrationRequestBody.getLastName();
       String email = userRegistrationRequestBody.getEmail();
       String password = userRegistrationRequestBody.getPassword();
       String roleStr = userRegistrationRequestBody.getRole();
       String role=null;

       if(firstName == null || firstName.isEmpty()){
           throw new InvalidInputException("Invalid firstName "+firstName);
       }
        if(lastName == null || lastName.isEmpty()){
            throw new InvalidInputException("Invalid lastName "+lastName);
        }
        if(email == null || email.isEmpty()){
            throw new InvalidInputException("Invalid email "+email);
        }
        if(password==null || password.isEmpty()){
            throw new InvalidInputException("Invalid password "+password);
        }
        if(roleStr == null || roleStr.isEmpty() || !roleStr.equalsIgnoreCase("admin")){
            role="USER";
        }else{
            role="ADMIN";
        }


        if(iUserRepository.findByEmail(email)!=null){
            log.debug("User with email id "+email+" is registered already");
            throw new InvalidInputException("User with email id "+email+" is registered already");
        }

       User rentReadUser=new User(firstName,lastName,email,passwordEncoder.encode(password),role);
        User savedRentReadUser=iUserRepository.save(rentReadUser);
        log.info("Registered user "+rentReadUser.getFirstName()+" "+rentReadUser.getLastName()+" with email "+rentReadUser.getEmail()+" successfully");

        return savedRentReadUser;
    }
}
