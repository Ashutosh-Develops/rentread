package com.github.saiyamn.rentread.controller;

import com.github.saiyamn.rentread.exception.BookNotFoundException;
import com.github.saiyamn.rentread.exception.BookRentException;
import com.github.saiyamn.rentread.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity invalidInputExceptionHandler(){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value= BookNotFoundException.class)
    public ResponseEntity handleBookNotFoundException(){
         return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value= BookRentException.class)
    public ResponseEntity handleBookRentException(){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value=RuntimeException.class)
    public ResponseEntity handleOtherRuntimeException(){
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
