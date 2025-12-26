package uz.app.modules.todo;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.app.modules.user.entity.User;
import uz.app.modules.user.repository.UserRepository;
import uz.app.modules.todo.dto.TodoDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class TodoResourceTest {

    @Inject
    UserRepository userRepository;

    Long userId;

    @BeforeEach
    @Transactional
    public void setup() {
        // Clean up or ensure unique user
        User user = new User();
        user.name = "Test User";
        user.email = "test_" + System.currentTimeMillis() + "@example.com";
        userRepository.persist(user);
        userId = user.id;
    }

    @Test
    public void testTodoLifecycle() {
        TodoDto.create createDto = new TodoDto.create("Test Todo", "Description", userId);

        // CREATE
        Number id = given()
            .contentType("application/json")
            .body(createDto)
            .when()
            .post("/todos/create")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .extract().path("id");

        Long todoId = id.longValue();

        // READ (List)
        given()
            .when()
            .get("/todos")
            .then()
            .statusCode(200);
            // .body("$.size()", is(1)); // Skipping size check as other tests/seeds might affect it

        // READ (Get)
        given()
            .when()
            .get("/todos/" + todoId)
            .then()
            .statusCode(200)
            .body("title", is("Test Todo"));

        // UPDATE
        TodoDto.create updateDto = new TodoDto.create("Updated Todo", "Updated Description", userId);
        given()
            .contentType("application/json")
            .body(updateDto)
            .when()
            .put("/todos/" + todoId)
            .then()
            .statusCode(200)
            .body("title", is("Updated Todo"));

        // DELETE
        given()
            .when()
            .delete("/todos/" + todoId)
            .then()
            .statusCode(204);

        // Verify Delete
        given()
            .when()
            .get("/todos/" + todoId)
            .then()
            .statusCode(404);
    }
}
