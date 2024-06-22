package com.example.encryptservice;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EncryptController {
    Encoder encoder = new Encoder();


    @PostMapping("/encrypt")
    public ResponseEntity<String> encode(@RequestBody(required = false) String str) throws BadRequestEx {
        String encodedStr = encoder.encode(str);
        System.out.println("2");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(encodedStr);
    }

}
