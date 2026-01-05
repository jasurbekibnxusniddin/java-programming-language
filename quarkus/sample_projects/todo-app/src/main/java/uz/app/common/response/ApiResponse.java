package uz.app.common.response;

import uz.app.common.exception.ErrorResponse;

public record ApiResponse<T>(
    String message,
    T data,
    ErrorResponse error
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, ErrorResponse error) {
        return new ApiResponse<>(message, null, error);
    }
}
