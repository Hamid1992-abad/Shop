package com.example.online.api.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotfoundExceptions extends RuntimeException{
    public CustomNotfoundExceptions(String message) {
        super(message);
    }

}
