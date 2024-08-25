package com.github.saiyamn.rentread.service.book;

import com.github.saiyamn.rentread.dto.book.BookRequestBody;
import com.github.saiyamn.rentread.entity.Book;
import com.github.saiyamn.rentread.entity.Rent;
import com.github.saiyamn.rentread.entity.User;

import java.util.List;

public interface IBookService {

    public Book createBook(BookRequestBody bookRequestBody);

    public Book deleteBook(String bookId);

    public List<Book> getBooksByStatus(String availabilityStatus);

    public Rent rentBook(String bookId);

    public Rent returnBook(String rentId);


}
