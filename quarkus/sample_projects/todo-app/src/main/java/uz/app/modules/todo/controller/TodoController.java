package uz.app.modules.todo.controller;

import java.net.URI;
import java.util.List;

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
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uz.app.modules.todo.dto.TodoDto;
import uz.app.modules.todo.service.TodoService;

@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Todo", description = "Todo operations")
public class TodoController {

    @Inject
    TodoService todoService;

    @POST
    @Path("/create")
    public Response create(TodoDto.create payload) {

        final TodoDto.response created = todoService.create(payload);
        return Response.created(URI.create("/todos/" + created.id())).entity(created).build();
    }

    @GET
    public List<TodoDto.response> list() {
        return todoService.list();
    }

    @GET
    @Path("/{id}")
    public TodoDto.response get(@PathParam("id") Long id) {
        return todoService.get(id);
    }

    @PUT
    @Path("/{id}")
    public TodoDto.response update(@PathParam("id") Long id, TodoDto.create payload) {
        return todoService.update(id, payload);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        todoService.delete(id);
        return Response.noContent().build();
    }
}
