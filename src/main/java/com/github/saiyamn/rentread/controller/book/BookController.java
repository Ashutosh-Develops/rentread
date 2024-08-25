package com.github.saiyamn.rentread.controller.book;

import com.github.saiyamn.rentread.dto.book.BookRequestBody;
import com.github.saiyamn.rentread.dto.bookrent.response.RentDetailsResponseBody;
import com.github.saiyamn.rentread.entity.Book;
import com.github.saiyamn.rentread.service.book.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
   private IBookService iBookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
   public Book AddBook(@RequestBody BookRequestBody bookRequestBody){
      return iBookService.createBook(bookRequestBody);
   }

   @PreAuthorize("hasAnyRole('USER','ADMIN')")
   @GetMapping
   public List<Book> getBooksAvailable(){
       return iBookService.getBooksByStatus("available");
   }

   @PreAuthorize("hasRole('ADMIN')")
   @DeleteMapping("/{bookId}")
   public Book deleteBook(@PathVariable String bookId){
      return iBookService.deleteBook(bookId);
   }

   @PreAuthorize("hasAnyRole('USER','ADMIN')")
   @PostMapping("/{bookId}/rent")
   public ResponseEntity<RentDetailsResponseBody> rentBook(@PathVariable String bookId){
       RentDetailsResponseBody responseBody=new RentDetailsResponseBody(iBookService.rentBook(bookId));
       return new ResponseEntity<RentDetailsResponseBody>(responseBody, HttpStatus.OK);
   }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
   @PostMapping("/{bookId}/return")
   public ResponseEntity<RentDetailsResponseBody> returnBook(@PathVariable String bookId){
        RentDetailsResponseBody responseBody=new RentDetailsResponseBody(iBookService.returnBook(bookId));
        return new ResponseEntity<RentDetailsResponseBody>(responseBody,HttpStatus.OK);
   }
}
