package com.example.personservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictEx extends RuntimeException  {
    public ConflictEx(String message) {
        super(message);
    }
}