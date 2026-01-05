package uz.app.modules.todo.controller;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uz.app.common.response.ApiResponse;
import uz.app.modules.todo.dto.TodoDto;
import uz.app.modules.todo.service.TodoService;

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Todo", description = "Todo operations")
// @Authenticated
public class TodoController {

    @Inject
    TodoService todoService;

    @POST
    @Path("/create")
    public Response create(TodoDto.create payload) {

        final TodoDto.response created = todoService.create(payload);
        return Response.created(URI.create("/todos/" + created.id()))
            .entity(ApiResponse.success("Todo created successfully", created))
            .build();
    }

    @GET
    public Response list(
            @jakarta.ws.rs.QueryParam("userId") Long userId,
            @jakarta.ws.rs.QueryParam("completed") Boolean completed,
            @jakarta.ws.rs.QueryParam("from") java.time.LocalDateTime from,
            @jakarta.ws.rs.QueryParam("to") java.time.LocalDateTime to) {
        List<TodoDto.response> todos = todoService.list(new TodoDto.Criteria(userId, completed, from, to));
        return Response.ok(ApiResponse.success("Todos retrieved successfully", todos)).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        TodoDto.response todo = todoService.get(id);
        return Response.ok(ApiResponse.success("Todo retrieved successfully", todo)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, TodoDto.create payload) {
        TodoDto.response updated = todoService.update(id, payload);
        return Response.ok(ApiResponse.success("Todo updated successfully", updated)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        todoService.delete(id);
        return Response.ok(ApiResponse.success("Todo deleted successfully", null)).build();
    }
}
