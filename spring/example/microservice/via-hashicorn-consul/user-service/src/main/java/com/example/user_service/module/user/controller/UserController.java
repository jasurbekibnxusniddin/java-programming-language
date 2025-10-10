package com.example.user_service.module.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/users")
    public List<String> users() {
        return List.of("User 1", "User 2", "User 3");
    }
}
