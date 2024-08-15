package com.example.online.api.exeptions.handlers;


import com.example.online.api.exeptions.CustomNotfoundExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalApiHandlerException {



        @ExceptionHandler(CustomNotfoundExceptions.class)
        public ResponseEntity handleNotFoundException(CustomNotfoundExceptions ex) {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", ex.getMessage());


             return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

            @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)

    {
        Map responseBody=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                (error) -> {
                    String attributeField= ((FieldError)error).getField();
                 String message=error.getDefaultMessage();
                    responseBody.put(attributeField,message);

                }
        );


        return new ResponseEntity(responseBody,HttpStatus.BAD_REQUEST);
    }
}

