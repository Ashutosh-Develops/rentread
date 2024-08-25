package com.github.saiyamn.rentread.dto.bookrent.response;

import com.github.saiyamn.rentread.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {

    private long userId;
    private String firstName;
    private String lastName;
    private String email;


    private UserInfo(User user){
        this.userId=user.getId();
        this.firstName=user.getFirstName();
        this.lastName=user.getLastName();
        this.email=user.getEmail();
    }

    public static UserInfo getUserInfoInstance(User user){
        return new UserInfo(user);
    }

}
