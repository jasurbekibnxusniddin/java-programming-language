package uz.app.modules.auth.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import uz.app.common.response.ApiResponse;
import uz.app.modules.auth.dto.AuthDto;
import uz.app.modules.auth.service.AuthService;
import uz.app.modules.user.dto.UserDto;

@Path("/auth")
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(AuthDto.loginRequest request) {

        if (request.email() == null || request.email().isBlank() || request.password() == null || request.password().isBlank()) {
            return Response.status(400).entity(ApiResponse.error("Email and password must be provided", null)).build();
        }

        AuthDto.loginResponse response = authService.login(request);
        return Response.ok(ApiResponse.success("Login successful", response)).build();
    }

    @POST
    @Path("/signup")
    public Response signup(AuthDto.signupRequest request) {

        if (request.email() == null || request.email().isBlank() || request.password() == null || request.password().isBlank()) {
            return Response.status(400).entity(ApiResponse.error("Email and password must be provided", null)).build();
        }

        UserDto.response created = authService.signup(request);
        return Response.status(201).entity(ApiResponse.success("User registered successfully", created)).build();
    }
}
