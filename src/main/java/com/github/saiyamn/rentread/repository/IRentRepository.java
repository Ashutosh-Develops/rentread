package com.github.saiyamn.rentread.repository;

import com.github.saiyamn.rentread.entity.Rent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRentRepository extends CrudRepository<Rent,Long> {
}
