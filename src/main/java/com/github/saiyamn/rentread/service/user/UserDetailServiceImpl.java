package com.github.saiyamn.rentread.service.user;

import com.github.saiyamn.rentread.entity.User;
import com.github.saiyamn.rentread.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        String email= username;
        User rentReaduser = iUserRepository.findByEmail(email);

        if(rentReaduser==null){
            throw new UsernameNotFoundException("User with email id "+email+" not found");
        }

        return org.springframework.security.core.userdetails.User.withUsername(email)
                .password(rentReaduser.getPassword())
                .roles(new String[]{rentReaduser.getRole().toString()})
                .build();
    }
}
