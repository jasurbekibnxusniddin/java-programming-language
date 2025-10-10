package com.example.order_service.module.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getOrders() {
        // use service name; Consul + Spring Cloud will resolve it
        String users = restTemplate.getForObject("http://user-service/users", String.class);
        return "Orders, users: " + users;
    }
}
