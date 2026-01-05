package uz.app.common.exception;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import uz.app.common.response.ApiResponse;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof NotFoundException) {
            return buildResponse(Response.Status.NOT_FOUND, exception.getMessage());
        }
        if (exception instanceof BadRequestException) {
            return buildResponse(Response.Status.BAD_REQUEST, exception.getMessage());
        }
        if (exception instanceof IllegalArgumentException) {
            return buildResponse(Response.Status.BAD_REQUEST, exception.getMessage());
        }
        if (exception instanceof NotAuthorizedException) {
            return buildResponse(Response.Status.UNAUTHORIZED, exception.getMessage());
        }
        
        
        // Log the error here if logger is available
        exception.printStackTrace(); 
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, "Internal Server Error: " + exception.getMessage());
    }

    private Response buildResponse(Response.Status status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.getStatusCode(), message);
        ApiResponse<Void> apiResponse = ApiResponse.error(message, errorResponse);
        return Response.status(status).entity(apiResponse).build();
    }
}
