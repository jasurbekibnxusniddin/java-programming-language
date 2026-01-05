package uz.app.modules.auth.service;

import java.util.Set;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import uz.app.modules.auth.dto.AuthDto;
import uz.app.modules.user.dto.UserDto;
import uz.app.modules.user.entity.User;
import uz.app.modules.user.repository.UserRepository;
import uz.app.modules.user.service.UserService;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    public AuthDto.loginResponse login(AuthDto.loginRequest request) {
        User user = userRepository.find("email", request.email()).firstResult();

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!BcryptUtil.matches(request.password(), user.password)) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        String token = tokenService.generateToken(user.email, Set.of(user.role));
        return new AuthDto.loginResponse(token);
    }
    
    public UserDto.response signup(AuthDto.signupRequest request) {
        UserDto.create createDto = new UserDto.create(request.name(), request.email(), request.password());
        return userService.create(createDto);
    }
}
