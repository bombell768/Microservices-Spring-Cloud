package com.example.personservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestEx extends Exception {
    public BadRequestEx() {
        super("This person already exists");
    }

    public BadRequestEx(int id) {
        super(String.valueOf(id));
    }
}
