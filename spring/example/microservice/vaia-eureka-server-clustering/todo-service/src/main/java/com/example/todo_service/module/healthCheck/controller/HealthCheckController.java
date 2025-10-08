package com.example.todo_service.module.healthCheck.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo_service.module.healthCheck.dto.HealthCheckDto;

@RestController
@RequestMapping("/health_check")
public class HealthCheckController {

    public HealthCheckController() {
    }

    @RequestMapping
    public HealthCheckDto.HealthCheckResponse healthCheck() {
        return new HealthCheckDto.HealthCheckResponse("Todo Service is running");
    }
}
