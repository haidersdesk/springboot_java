package com.example.lager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="This is a Bad request")
public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg){
             super(msg);
    }

}
