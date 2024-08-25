package com.github.saiyamn.rentread.service.book;

import com.github.saiyamn.rentread.dto.book.BookRequestBody;
import com.github.saiyamn.rentread.entity.*;
import com.github.saiyamn.rentread.exception.BookNotFoundException;
import com.github.saiyamn.rentread.exception.BookRentException;
import com.github.saiyamn.rentread.exception.InvalidInputException;
import com.github.saiyamn.rentread.repository.IBookRepository;
import com.github.saiyamn.rentread.repository.IRentRepository;
import com.github.saiyamn.rentread.repository.IUserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@Service
public class BookServiceImpl implements  IBookService{

    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IBookRepository iBookRepository;
    @Autowired
    private IRentRepository iRentRepository;

    @Override
    public Book createBook(BookRequestBody bookRequestBody) {

        String title = bookRequestBody.getTitle();
        String author= bookRequestBody.getAuthor();
        String genre = bookRequestBody.getGenre();
        String availabilityStatus = bookRequestBody.getAvailabilityStatus();

        if(title==null || title.isEmpty()){
            log.debug("Invalid book title "+title);
            throw new InvalidInputException("Invalid book title "+title);
        }

        if(author==null || author.isEmpty()){
            log.debug("Invalid book author "+author);
            throw new InvalidInputException("Invalid book author "+author);
        }

        Book book=new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setGenre(getGenre(genre));
        book.setAvailabilityStatus(getAvailabilityStatus(availabilityStatus));
        Book savedBook= iBookRepository.save(book);
        log.info("Book with book id "+savedBook.getId()+"added successfully");

        return savedBook;
    }

    @Override
    public Book deleteBook(String bookId) {

        if(bookId==null||bookId.isEmpty()){
            log.debug("Invalid book id "+bookId);
            throw new InvalidInputException("Invalid book id "+bookId);
        }

        Optional<Book> book=iBookRepository.findById(Long.parseLong(bookId));
        User authenticatedUser = getAuthenticatedUser();

        if(book.isEmpty()){
            log.debug("Book with book id "+bookId+" not found");
            throw new BookNotFoundException("Book with book id "+bookId+" not found");
        }

        iBookRepository.delete(book.get());
        Book deletedBook=book.get();
        log.info("Book with id "+bookId+" deleted successfully by Admin "+authenticatedUser.getFirstName()+" "+authenticatedUser.getLastName()+" with id "+authenticatedUser.getId());

        return deletedBook;
    }

    @Override
    public List<Book> getBooksByStatus(String availabilityStatus) {

        StringBuilder searchByBuilder=new StringBuilder();
        if(availabilityStatus==null||availabilityStatus.isEmpty()||availabilityStatus.equalsIgnoreCase("available")){
            searchByBuilder.append("AVAILABLE");
        }else{
            searchByBuilder.append("NOT_AVAILABLE");
        }

        String searchTerm = searchByBuilder.toString();
        List<Book> books= (List<Book>) iBookRepository.findAll();
        List<Book> booksListByStatus=new ArrayList<>();

        for(Book book:books){
             if(book.getAvailabilityStatus().toString().equalsIgnoreCase(searchTerm)){
                 booksListByStatus.add(book);
             }
        }

        return booksListByStatus;
    }

    @Override
    @Transactional
    public Rent rentBook(String bookId) {

        if(bookId==null || bookId.isEmpty()){
            throw new InvalidInputException("Invalid book id "+bookId);
        }

        Optional<Book> bookOptional=iBookRepository.findById(Long.parseLong(bookId));

        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("book with book id "+bookId+" does not exist");
        }

        User authenticatedUser = getAuthenticatedUser();
        Book bookToBeRented=bookOptional.get();
        if(bookToBeRented.getAvailabilityStatus().equals(AvailabilityStatus.NOT_AVAILABLE)){
            throw new BookRentException("Book with book id "+bookId+" is not available for rent");
        }

        // check if user has 2 active rentals, if yes then user cannot rent
        List<Rent> rents= (List<Rent>) iRentRepository.findAll();
        int numOfRents=0;
        for(Rent rent:rents){
            if(rent.getUser().getId()== authenticatedUser.getId()&&rent.getRentStatus().equals(RentStatus.ACTIVE)){
                ++numOfRents;
                if(numOfRents==2) {
                    throw new BookRentException("User with user id "+getAuthenticatedUser().getId()+" already has 2 active rentals");
                }
            }
        }

        Rent rentBook=new Rent();
        rentBook.setBook(bookToBeRented);
        rentBook.setUser(authenticatedUser);
        rentBook.setRentStatus(RentStatus.ACTIVE);
        Rent savedRentBook=iRentRepository.save(rentBook);
        bookToBeRented.setAvailabilityStatus(AvailabilityStatus.NOT_AVAILABLE);
        iBookRepository.save(bookToBeRented);

        log.info("User "+authenticatedUser.getFirstName()+" "+authenticatedUser.getLastName()+" with id "+authenticatedUser.getId()+" successfully rented book "+bookToBeRented.getTitle()+" with id "+bookToBeRented.getId());
        return savedRentBook;
    }

    @Override
    @Transactional
    public Rent returnBook(String bookId) {

        if(bookId==null || bookId.isEmpty()){
            throw new InvalidInputException("Invalid book id "+bookId);
        }

        Optional<Book> bookOptional=iBookRepository.findById(Long.parseLong(bookId));
        if(bookOptional.isEmpty()){
            throw new BookNotFoundException("Book with book id"+bookId+" does not exist");
        }

        User authenticatedUser = getAuthenticatedUser();
        List<Rent> rents= (List<Rent>) iRentRepository.findAll();
        Rent rentedBookDetails=null;
        for(Rent rent:rents){
            if(rent.getUser().getId()==authenticatedUser.getId()&&rent.getBook().getId()==bookOptional.get().getId()){
                rentedBookDetails=rent;
                break;
            }
        }

        if(rentedBookDetails==null){
            throw new BookRentException("User with id "+authenticatedUser.getId()+" has not rented book with id "+bookId);
        }

        Book returnedBook=rentedBookDetails.getBook();
        iRentRepository.delete(rentedBookDetails);
        returnedBook.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        iBookRepository.save(returnedBook);
        log.info("User "+authenticatedUser.getFirstName()+" "+authenticatedUser.getLastName()+" with id "+authenticatedUser.getId()+" has returned book "+returnedBook.getTitle()+" with id "+returnedBook.getId());
        return rentedBookDetails;
    }


    private Genre getGenre(String genre){

        if(genre==null || genre.isEmpty()){
            return Genre.OTHER;
        }

        if(genre.equalsIgnoreCase("horror")){
            return Genre.HORROR;
        }

        if(genre.equalsIgnoreCase("romantic")){
            return Genre.ROMANTIC;
        }

        if(genre.equalsIgnoreCase("scifi")){
            return Genre.SCI_FI;
        }

        return Genre.OTHER;
    }

    private AvailabilityStatus getAvailabilityStatus(String status){

        if(status == null || status.isEmpty()){
            return AvailabilityStatus.NOT_AVAILABLE;
        }

        if(status.equalsIgnoreCase("available")){
            return AvailabilityStatus.AVAILABLE;
        }

        return AvailabilityStatus.NOT_AVAILABLE;
    }

    private User getAuthenticatedUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User loggedInUser = iUserRepository.findByEmail(userDetails.getUsername());
        if(loggedInUser==null){
            throw new RuntimeException("logged in user does not exist "+loggedInUser);
        }

        return loggedInUser;
    }
}
