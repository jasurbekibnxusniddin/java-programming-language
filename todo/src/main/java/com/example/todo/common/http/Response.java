package com.example.todo.common.http;



import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private final boolean success;
    private final T data;
    private final String message;
    private final Integer status;

    public Response(boolean success, T data, String message, HttpStatus status) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.status = status.value();
    }

    // Factory helpers
    public static <T> Response<T> ok(T data) {
        return new Response<>(true, data, null, HttpStatus.OK);
    }

    public static <T> Response<T> created(T data) {
        return new Response<>(true, data, null, HttpStatus.CREATED);
    }

    public static <T> Response<T> error(String message, HttpStatus status) {
        return new Response<>(false, null, message, status);
    }

    // Getters (needed for JSON serialization)
    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public String getMessage() { return message; }
    public Integer getStatus() { return status; }
}
