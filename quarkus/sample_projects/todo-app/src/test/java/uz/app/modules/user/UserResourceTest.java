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
        UserDto.create createDto = new UserDto.create("Test User", email, password);

        // CREATE (Public)
        Number id = given()
            .contentType("application/json")
            .body(createDto)
            .when()
            .post("/users/create")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("email", is(email))
            .extract().path("id");

        Long userId = id.longValue();

        // READ (List) - Secured
        given()
            .auth().preemptive().basic(email, password)
            .when()
            .get("/users")
            .then()
            .statusCode(200);

        // READ (Get) - Secured
        given()
            .auth().preemptive().basic(email, password)
            .when()
            .get("/users/" + userId)
            .then()
            .statusCode(200)
            .body("name", is("Test User"));

        // UPDATE - Secured
        UserDto.update updateDto = new UserDto.update("Updated User", null);
        given()
            .auth().preemptive().basic(email, password)
            .contentType("application/json")
            .body(updateDto)
            .when()
            .put("/users/" + userId)
            .then()
            .statusCode(200)
            .body("name", is("Updated User"));

        // DELETE - Secured
        given()
            .auth().preemptive().basic(email, password)
            .when()
            .delete("/users/" + userId)
            .then()
            .statusCode(204);

        // Verify Delete (User is deleted, so auth should fail)
        given()
            .auth().preemptive().basic(email, password)
            .when()
            .get("/users/" + userId)
            .then()
            .statusCode(401);
    }
}
