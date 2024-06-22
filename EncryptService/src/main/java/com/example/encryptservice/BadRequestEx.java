package com.example.encryptservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestEx extends Exception {
    public BadRequestEx() {
        super("Error");
    }

    public BadRequestEx(int id) {
        super(String.valueOf(id));
    }
}
