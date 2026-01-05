package uz.app.modules.user.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uz.app.common.response.ApiResponse;
import uz.app.modules.user.dto.UserDto;
import uz.app.modules.user.service.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "User", description = "User operations")

public class UserController {

    @Inject
    UserService userService;

    @GET
    public Response list(@QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        List<UserDto.response> users = userService.list(page, size);
        return Response.ok(ApiResponse.success("Users retrieved successfully", users)).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        UserDto.response user = userService.get(id);
        return Response.ok(ApiResponse.success("User retrieved successfully", user)).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, UserDto.update payload) {
        UserDto.response updated = userService.update(id, payload);
        return Response.ok(ApiResponse.success("User updated successfully", updated)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.ok(ApiResponse.success("User deleted successfully", null)).build();
    }
}
