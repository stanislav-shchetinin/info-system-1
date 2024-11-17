package ru.shchetinin.lab1p.excepion.handler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ru.shchetinin.lab1p.excepion.EndpointException;

@Provider
public class EndpointExceptionHandler implements ExceptionMapper<EndpointException> {
    @Override
    public Response toResponse(EndpointException e) {
        return Response.status(e.getStatus())
                .entity(e.getMessage())
                .build();
    }

}
