package com.github.saiyamn.rentread.book;

import com.github.saiyamn.rentread.entity.*;
import com.github.saiyamn.rentread.repository.IBookRepository;
import com.github.saiyamn.rentread.repository.IRentRepository;
import com.github.saiyamn.rentread.repository.IUserRepository;
import com.github.saiyamn.rentread.service.book.BookServiceImpl;
import com.github.saiyamn.rentread.service.book.IBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.mockito.Mockito.mockStatic;

public class BookServiceTest {


    @Test
    @DisplayName("Rent a book available to the user")
    public void rentAvailableBookByAnAuthorizedUser(){

        IBookRepository iBookRepository= Mockito.mock(IBookRepository.class);
        IRentRepository iRentRepository=Mockito.mock(IRentRepository.class);
        IUserRepository iUserRepository=Mockito.mock(IUserRepository.class);

        IBookService iBookService=new BookServiceImpl(iUserRepository,iBookRepository,iRentRepository);

        Optional<Book> bookOptional=getBookInstance();
        User authenticatedUser = getAUserInstance();
        long bookId = bookOptional.get().getId();

        Mockito.when(iBookRepository.findById(bookId)).thenReturn(bookOptional);

        Authentication authentication=getAuthentication();
        MockedStatic<SecurityContextHolder> mockSecurityContextHolder = mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

     //   mockSecurityContextHolder.when(() -> SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        Mockito.when(iUserRepository.findByEmail("ajax@gmail.com")).thenReturn(authenticatedUser);

       // Mockito.when(iBookService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        List<Rent> rents = new ArrayList<>();
        Mockito.when(iRentRepository.findAll()).thenReturn(rents);
        Rent rent = getBookRentDetails(authenticatedUser,bookOptional.get());
        Rent savedRent =new Rent(1l,authenticatedUser,bookOptional.get(),RentStatus.ACTIVE);


       Mockito.when(iRentRepository.save(Mockito.eq(rent))).thenReturn(savedRent);
       Mockito.when(iBookRepository.save(Mockito.eq(bookOptional.get()))).thenReturn(null);
       Rent rentBookReturnedByService=iBookService.rentBook(String.valueOf(bookId));

        Assertions.assertEquals(1l,rentBookReturnedByService.getId());
        Assertions.assertEquals("Life of Warrior",rentBookReturnedByService.getBook().getTitle());
        Assertions.assertEquals("Anonymous",rentBookReturnedByService.getBook().getAuthor());
        Assertions.assertEquals("ajax@gmail.com",rentBookReturnedByService.getUser().getEmail());
    }


    @Test
    @DisplayName("Return a book rented by a user")
    public void returnRentedBook(){

        IBookRepository iBookRepository= Mockito.mock(IBookRepository.class);
        IRentRepository iRentRepository=Mockito.mock(IRentRepository.class);
        IUserRepository iUserRepository=Mockito.mock(IUserRepository.class);

        IBookService iBookService=new BookServiceImpl(iUserRepository,iBookRepository,iRentRepository);

        Optional<Book> bookOptional=getBookInstance();
        User authenticatedUser = getAUserInstance();
        long bookId = bookOptional.get().getId();

        Mockito.when(iBookRepository.findById(bookId)).thenReturn(bookOptional);

        // Mock SecurityContextHolder used in getAuthenticatedUser() method

        Authentication authentication=getAuthentication();
        MockedStatic<SecurityContextHolder> mockSecurityContextHolder = mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

       // mockSecurityContextHolder.when(() -> SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        Mockito.when(iUserRepository.findByEmail("ajax@gmail.com")).thenReturn(authenticatedUser);


        Rent rent =getBookRentDetails(authenticatedUser,bookOptional.get());
        rent.setId(1l);
        List<Rent> rents=new ArrayList<>();
        rents.add(rent);
        Mockito.when(iRentRepository.findAll()).thenReturn(rents);
        Mockito.when(iBookRepository.save(Mockito.eq(rent.getBook()))).thenReturn(null);
        Rent rentedBookDetails=iBookService.returnBook(String.valueOf(bookId));

        Assertions.assertEquals(1l,rentedBookDetails.getId());
        Assertions.assertEquals("Life of Warrior",rentedBookDetails.getBook().getTitle());
        Assertions.assertEquals("Anonymous",rentedBookDetails.getBook().getAuthor());
        Assertions.assertEquals("ajax@gmail.com",rentedBookDetails.getUser().getEmail());
    }



    private Optional<Book> getBookInstance(){

        Book book=new Book();
        book.setId(1l);
        book.setTitle("Life of Warrior");
        book.setAuthor("Anonymous");
        book.setGenre(Genre.OTHER);
        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        return Optional.of(book);
    }


    private User getAUserInstance(){
        User user = new User();
        user.setId(1l);
        user.setFirstName("Ajax");
        user.setLastName("Akaminatsu");
        user.setPassword("Ajax123");
        user.setEmail("ajax@gmail.com");
        user.setRole("USER");
        user.setBooksRented(Arrays.asList());

        return user;
    }

    private Rent getBookRentDetails(User user,Book book){

        Rent rent=new Rent();
        rent.setUser(user);
        rent.setBook(book);
        rent.setRentStatus(RentStatus.ACTIVE);

        return rent;
    }

   private Authentication getAuthentication(){


        UserDetails userDetails =new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return "ajax@gmail.com";
            }
        };

       Authentication authentication=new Authentication() {
           @Override
           public Collection<? extends GrantedAuthority> getAuthorities() {
               return null;
           }

           @Override
           public Object getCredentials() {
               return null;
           }

           @Override
           public Object getDetails() {
               return null;
           }

           @Override
           public Object getPrincipal() {
               return userDetails;
           }

           @Override
           public boolean isAuthenticated() {
               return false;
           }

           @Override
           public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

           }

           @Override
           public String getName() {
               return null;
           }
       };

      return authentication;

   }
}
