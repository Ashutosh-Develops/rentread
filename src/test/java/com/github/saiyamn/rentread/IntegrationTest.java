package com.github.saiyamn.rentread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.saiyamn.rentread.dto.bookrent.response.RentDetailsResponseBody;
import com.github.saiyamn.rentread.entity.*;
import com.github.saiyamn.rentread.repository.IBookRepository;
import com.github.saiyamn.rentread.repository.IRentRepository;
import com.github.saiyamn.rentread.repository.IUserRepository;
import jakarta.persistence.Temporal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private IRentRepository iRentRepository;
    @Autowired
    private IBookRepository iBookRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    private static PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();


    @Test
    public void rentBook() throws Exception {

       String API_PATH="/books/{bookId}/rent";

        // Create a test User

        String testPassword="W@idnvnjnfjdfheuriejirjj23232323lkldfjkidfk33###invknkdfndf";
        User user = createTestUser(testPassword);

        // Create a testbook
        Book book = createTestBook();

        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.post(API_PATH,book.getId())
                .with(httpBasic(user.getEmail(),testPassword))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        RentDetailsResponseBody responseBody=objectMapper.readValue(result.getResponse().getContentAsString(),RentDetailsResponseBody.class);

        Assertions.assertNotNull(responseBody);

        Assertions.assertEquals(user.getFirstName(),responseBody.getUserInfo().getFirstName());
        Assertions.assertEquals(user.getEmail(),responseBody.getUserInfo().getEmail());
        Assertions.assertEquals(book.getTitle(),responseBody.getBookInfo().getTitle());
        Assertions.assertEquals(book.getAuthor(),responseBody.getBookInfo().getAuthor());

        // Delete Test User, Test book, and rent details created as part of the testing from database
        deleteTestActors(responseBody.getRentId());
    }


    @Test
    public void returnRentedBook() throws Exception {

        String API_PATH="/books/{bookId}/return";

        // Create a test User
        String testPassword="W@idnvnjnfjdfheuriejirjj23232323lkldfjkidfk33###invknkdfndf";
        User user = createTestUser(testPassword);

        // Create a testbook
        Book book = createTestBook();

        // Create Test Rent
        Rent rent= createTestRentInstance(user,book);

        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.post(API_PATH,book.getId())
                .with(httpBasic(user.getEmail(),testPassword))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        RentDetailsResponseBody responseBody=objectMapper.readValue(result.getResponse().getContentAsString(),RentDetailsResponseBody.class);

        Assertions.assertNotNull(responseBody);

        Assertions.assertEquals(user.getFirstName(),responseBody.getUserInfo().getFirstName());
        Assertions.assertEquals(user.getEmail(),responseBody.getUserInfo().getEmail());
        Assertions.assertEquals(book.getTitle(),responseBody.getBookInfo().getTitle());
        Assertions.assertEquals(book.getAuthor(),responseBody.getBookInfo().getAuthor());

        // When a return request is made to already returned book then 400 status code should be returned
        MvcResult rentReturnedBookResult=mockMvc.perform(MockMvcRequestBuilders.post(API_PATH,book.getId())
                        .with(httpBasic(user.getEmail(),testPassword))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),rentReturnedBookResult.getResponse().getStatus());


        // Delete test actors (User,Book) from the database
        deleteTestUser(user);
        deleteTestBook(book);
    }

    private User createTestUser(String password){

        User user=new User();
        user.setFirstName("Sehdev");
        user.setLastName("Pandav");
        user.setEmail("sehdev@indraprastha.org");
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ADMIN");

        return iUserRepository.save(user);
    }

    private Book createTestBook(){
        Book book=new Book();
        book.setTitle("Manav Sabhyata aur pargrahi");
        book.setAuthor("Shyam Das");
        book.setGenre(Genre.SCI_FI);
        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        return iBookRepository.save(book);
    }

    private Rent createTestRentInstance(User user,Book book){

        Rent rent=new Rent();
        rent.setUser(user);
        rent.setBook(book);
        rent.setRentStatus(RentStatus.ACTIVE);

        return iRentRepository.save(rent);
    }

    private void deleteTestActors(long rentId){

        Optional<Rent> rent=iRentRepository.findById(rentId);

        User user = rent.get().getUser();
        Book book = rent.get().getBook();

         iRentRepository.deleteById(rentId);
        iUserRepository.delete(user);
        iBookRepository.delete(book);
    }

    private void deleteTestUser(User user){

        iUserRepository.delete(user);
    }

    private void deleteTestBook(Book book){

        iBookRepository.delete(book);
    }
}
