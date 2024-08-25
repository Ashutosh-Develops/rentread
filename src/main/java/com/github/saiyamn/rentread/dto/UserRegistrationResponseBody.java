package com.github.saiyamn.rentread.dto;

import com.github.saiyamn.rentread.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegistrationResponseBody {

    private long id;
    private String firstName;
    private String lastName;
    private String email;

    public UserRegistrationResponseBody(User user){
        this.id=user.getId();
        this.firstName=user.getFirstName();
        this.lastName=user.getLastName();
        this.email=user.getEmail();
    }
}
