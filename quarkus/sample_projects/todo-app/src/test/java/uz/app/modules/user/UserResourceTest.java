package uz.app.modules.user;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import uz.app.modules.user.dto.UserDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class UserResourceTest {

    @Test
    public void testUserLifecycle() {
        String email = "testuser_" + System.currentTimeMillis() + "@example.com";
        String password = "password";
        // Create user via Repository directly for this test
        // Since we removed the public create endpoint from UserController
        // we need to set up the data manually or via Auth endpoint (which we test separately)
        
        uz.app.modules.user.entity.User user = new uz.app.modules.user.entity.User();
        user.name = "Test User";
        user.email = email;
        user.password = io.quarkus.elytron.security.common.BcryptUtil.bcryptHash(password);
        user.role = "user";
        
        // We need a transaction to persist
        // Ideally we should use a helper method or @TransactionalSetup
        // But for simplicity in this flow, we might assume the user exists
        // actually let's just create it at the start of the test if we can interact with repo
        
        // However, UserResourceTest is integration test. 
        // Let's use the AUTH endpoint to create the user! 
        
        uz.app.modules.auth.dto.AuthDto.signupRequest signupRequest = 
            new uz.app.modules.auth.dto.AuthDto.signupRequest("Test User", email, password);
            
        Number id = given()
            .contentType("application/json")
            .body(signupRequest)
            .when()
            .post("/auth/signup")
            .then()
            .statusCode(201)
            .body("data.id", notNullValue())
            .body("data.email", is(email))
            .extract().path("data.id");

        Long userId = id.longValue();

        // READ (List) - Secured
        given()
            .when()
            .get("/users")
            .then()
            .statusCode(200);

        // READ (Get) - Secured
        given()
            .when()
            .get("/users/" + userId)
            .then()
            .statusCode(200)
            .statusCode(200)
            .body("data.name", is("Test User"));

        // UPDATE - Secured
        UserDto.update updateDto = new UserDto.update("Updated User", null);
        given()
            .contentType("application/json")
            .body(updateDto)
            .when()
            .put("/users/" + userId)
            .then()
            .statusCode(200)
            .statusCode(200)
            .body("data.name", is("Updated User"));

        // DELETE - Secured
        given()
            .when()
            .delete("/users/" + userId)
            .then()
            .statusCode(200);

        // Verify Delete (User is deleted, so 404)
        given()
            .when()
            .get("/users/" + userId)
            .then()
            .statusCode(404);
    }
}
