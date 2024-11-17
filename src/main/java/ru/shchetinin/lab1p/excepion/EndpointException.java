package ru.shchetinin.lab1p.excepion;

import jakarta.ws.rs.GET;
import lombok.Getter;

import static jakarta.ws.rs.core.Response.Status;

@Getter
public class EndpointException extends RuntimeException {
    private final String message;
    private final Status status;

    public EndpointException(Status status, String message) {
        this.message = message;
        this.status = status;
    }

}
