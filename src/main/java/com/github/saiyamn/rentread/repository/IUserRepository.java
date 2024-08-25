package com.github.saiyamn.rentread.repository;

import com.github.saiyamn.rentread.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User,Long> {

    User findByEmail(final String email);
}
