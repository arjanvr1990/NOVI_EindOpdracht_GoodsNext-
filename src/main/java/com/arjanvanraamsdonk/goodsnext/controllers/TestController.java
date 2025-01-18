package com.arjanvanraamsdonk.goodsnext.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> testEndpoint() {
        System.out.println("Test endpoint reached");
        return ResponseEntity.ok("Test endpoint is working");
    }
    @PostMapping
    public ResponseEntity<String> testEndpoint2() {
        System.out.println("Test endpoint reached");
        return ResponseEntity.ok("Test endpoint is working");
    }

}
