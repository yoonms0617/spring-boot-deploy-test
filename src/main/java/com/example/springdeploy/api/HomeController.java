package com.example.springdeploy.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("Hello");
    }

    @GetMapping("/world")
    public ResponseEntity<String> world() {
        return ResponseEntity.ok().body("World");
    }

}
