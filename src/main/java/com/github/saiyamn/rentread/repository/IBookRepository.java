package com.github.saiyamn.rentread.repository;

import com.github.saiyamn.rentread.entity.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends CrudRepository<Book,Long> {
}
