package uz.app.modules.auth;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.app.modules.auth.dto.AuthDto;
import uz.app.modules.user.entity.User;
import uz.app.modules.user.repository.UserRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class AuthResourceTest {

    @Inject
    UserRepository userRepository;

    String email = "auth_test@example.com";
    String password = "password123";

    @BeforeEach
    @Transactional
    public void setup() {
        if (userRepository.find("email", email).firstResult() == null) {
            User user = new User();
            user.name = "Auth User";
            user.email = email;
            // Matches UserService hashing
            user.password = io.quarkus.elytron.security.common.BcryptUtil.bcryptHash(password);
            user.role = "user";
            userRepository.persist(user);
        }
    }

    @Test
    public void testLogin() {
        AuthDto.loginRequest loginRequest = new AuthDto.loginRequest(email, password);

        given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(200)
            .body("data.token", notNullValue());
    }

    @Test
    public void testLoginInvalidCredentials() {
        AuthDto.loginRequest loginRequest = new AuthDto.loginRequest(email, "wrongpassword");

        given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(401);
    }

    @Test
    public void testLoginUserNotFound() {
        AuthDto.loginRequest loginRequest = new AuthDto.loginRequest("nonexistent@example.com", "password");

        given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post("/auth/login")
            .then()
            .statusCode(404);
    }

    @Test
    public void testSignup() {
        String newEmail = "signup_test_" + System.currentTimeMillis() + "@example.com";
        AuthDto.signupRequest request = new AuthDto.signupRequest("Signup User", newEmail, "password");

        given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/auth/signup")
            .then()
            .statusCode(201)
            .body("data.email", is(newEmail))
            .body("data.id", notNullValue());
            
        // Test Duplicate
        given()
            .contentType("application/json")
            .body(request)
            .when()
            .post("/auth/signup")
            .then()
            .statusCode(400); // Expecting BadRequest
    }
}
