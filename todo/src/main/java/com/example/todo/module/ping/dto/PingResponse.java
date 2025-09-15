package com.example.todo.module.ping.dto;

public class PingResponse {
    private String message;

    public PingResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
