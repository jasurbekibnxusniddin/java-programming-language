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
    String userEmail;
    String userPassword = "password";

    @BeforeEach
    @Transactional
    public void setup() {
        userEmail = "test_" + System.currentTimeMillis() + "@example.com";
        User user = new User();
        user.name = "Test User";
        user.email = userEmail;
        user.password = io.quarkus.elytron.security.common.BcryptUtil.bcryptHash(userPassword);
        user.role = "user";
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
            .statusCode(201)
            .body("data.id", notNullValue())
            .extract().path("data.id");

        Long todoId = id.longValue();

        // READ (List)
        given()
            .when()
            .get("/todos")
            .then()
            .statusCode(200);
            // .body("$.size()", is(1)); 

        // READ (Get)
        given()
            .when()
            .get("/todos/" + todoId)
            .then()
            .statusCode(200)
            .statusCode(200)
            .body("data.title", is("Test Todo"));

        // UPDATE
        TodoDto.create updateDto = new TodoDto.create("Updated Todo", "Updated Description", userId);
        given()
            .contentType("application/json")
            .body(updateDto)
            .when()
            .put("/todos/" + todoId)
            .then()
            .statusCode(200)
            .statusCode(200)
            .body("data.title", is("Updated Todo"));

        // DELETE
        given()
            .when()
            .delete("/todos/" + todoId)
            .then()
            .statusCode(200);

        // Verify Delete
        given()
            .when()
            .get("/todos/" + todoId)
            .then()
            .statusCode(404);
    }

    @Test
    public void testTodoFiltering() {
        // Create another user
        User otherUser = new User();
        otherUser.name = "Other User";
        otherUser.email = "other_" + System.currentTimeMillis() + "@example.com";
        otherUser.role = "user";
        // existing user is already persisted in setup
        
        // We need transactional context to persist, but tests are not transactional by default unless annotated.
        // The setup is transactional. Let's do this in the test body or rely on helper.
        // Actually, let's just use the existing user (userId) and maybe create another one via repo if needed.
        // But for simplicity, let's just rely on the existing user and create some todos.
        
        // We can create todos via API to ensure they are created properly
        
        TodoDto.create todo1 = new TodoDto.create("Todo 1", "Desc 1", userId);
        TodoDto.create todo2 = new TodoDto.create("Todo 2", "Desc 2", userId);
        
        given().contentType("application/json").body(todo1).when().post("/todos/create").then().statusCode(201);
        given().contentType("application/json").body(todo2).when().post("/todos/create").then().statusCode(201);
        
        // Filter by userId
        given()
            .queryParam("userId", userId)
            .when()
            .get("/todos")
            .then()
            .statusCode(200)
            .body("data.size()", org.hamcrest.Matchers.greaterThanOrEqualTo(2));
            
        // Filter by non-existent userId
        given()
            .queryParam("userId", 999999)
            .when()
            .get("/todos")
            .then()
            .statusCode(200)
            .body("data.size()", is(0));
            
        // Filter by completed status (default is false)
        given()
            .queryParam("completed", false)
            .when()
            .get("/todos")
            .then()
            .statusCode(200)
            .body("data.size()", org.hamcrest.Matchers.greaterThanOrEqualTo(2));
            
        given()
            .queryParam("completed", true)
            .when()
            .get("/todos")
            .then()
            .statusCode(200)
            .body("data.size()", is(0));
    }
}
