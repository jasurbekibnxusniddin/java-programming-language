package uz.app.modules.common.exception;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class ExceptionMapperTest {

    @Path("/test/exceptions")
    public static class TestExceptionResource {

        @GET
        @Path("/not-found")
        public void throwNotFound() {
            throw new NotFoundException("Resource not found");
        }

        @GET
        @Path("/bad-request")
        public void throwBadRequest() {
            throw new BadRequestException("Invalid request");
        }

        @GET
        @Path("/illegal-argument")
        public void throwIllegalArgument() {
            throw new IllegalArgumentException("Illegal argument provided");
        }

        @GET
        @Path("/generic")
        public void throwGeneric() {
            throw new RuntimeException("Unexpected error");
        }
    }

    @Test
    public void testNotFoundMapping() {
        given()
            .when().get("/test/exceptions/not-found")
            .then()
            .statusCode(404)
            .body("status", is(404))
            .body("message", is("Resource not found"))
            .body("timestamp", notNullValue());
    }

    @Test
    public void testBadRequestMapping() {
        given()
            .when().get("/test/exceptions/bad-request")
            .then()
            .statusCode(400)
            .body("status", is(400))
            .body("message", is("Invalid request"))
            .body("timestamp", notNullValue());
    }

    @Test
    public void testIllegalArgumentMapping() {
        given()
            .when().get("/test/exceptions/illegal-argument")
            .then()
            .statusCode(400)
            .body("status", is(400))
            .body("message", is("Illegal argument provided"))
            .body("timestamp", notNullValue());
    }

    @Test
    public void testGenericMapping() {
        given()
            .when().get("/test/exceptions/generic")
            .then()
            .statusCode(500)
            .body("status", is(500))
            .body("message", is("Internal Server Error"))
            .body("timestamp", notNullValue());
    }
}
