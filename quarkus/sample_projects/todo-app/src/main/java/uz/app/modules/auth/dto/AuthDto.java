package uz.app.modules.auth.dto;

public class AuthDto {

    public record loginRequest(
            String email,
            String password
            ) {

    }

    public record loginResponse(
            String token
            ) {

    }

    public record signupRequest(
            String name,
            String email,
            String password
            ) {

    }
}
