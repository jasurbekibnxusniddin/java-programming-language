package uz.app.modules.user.controller;

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
import uz.app.modules.user.dto.UserDto;
import uz.app.modules.user.service.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User", description = "User operations")
public class UserController {

    @Inject
    UserService userService;

    @POST
    @Path("/create")
    public Response create(UserDto.create payload) {
        UserDto.response created = userService.create(payload);
        return Response.created(URI.create("/users/" + created.id())).entity(created).build();
    }

    @GET
    public List<UserDto.response> list() {
        return userService.list();
    }

    @GET
    @Path("/{id}")
    public UserDto.response get(@PathParam("id") Long id) {
        return userService.get(id);
    }

    @PUT
    @Path("/{id}")
    public UserDto.response update(@PathParam("id") Long id, UserDto.update payload) {
        return userService.update(id, payload);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.noContent().build();
    }
}
