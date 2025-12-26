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
        UserDto.create createDto = new UserDto.create("Test User", email);

        // CREATE
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

        // READ (List)
        given()
            .when()
            .get("/users")
            .then()
            .statusCode(200);

        // READ (Get)
        given()
            .when()
            .get("/users/" + userId)
            .then()
            .statusCode(200)
            .body("name", is("Test User"));

        // UPDATE
        UserDto.update updateDto = new UserDto.update("Updated User", null);
        given()
            .contentType("application/json")
            .body(updateDto)
            .when()
            .put("/users/" + userId)
            .then()
            .statusCode(200)
            .body("name", is("Updated User"));

        // DELETE
        given()
            .when()
            .delete("/users/" + userId)
            .then()
            .statusCode(204);

        // Verify Delete
        given()
            .when()
            .get("/users/" + userId)
            .then()
            .statusCode(404);
    }
}
