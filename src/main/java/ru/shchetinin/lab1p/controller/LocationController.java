package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dto.request.LocationRequest;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.service.LocationService;

import java.util.List;

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class LocationController {

    @Inject
    private LocationService locationService;

    @POST
    public Response createLocation(@Valid LocationRequest location) {
        var createdLocation = locationService.createLocation(location);
        return Response.status(Response.Status.CREATED).entity(createdLocation).build();
    }

    @GET
    @Path("/{id}")
    public Response getLocationById(@PathParam("id") Long id) {
        var location = locationService.getLocationById(id);
        return Response.ok(location).build();
    }

    @GET
    @Path("/all")
    public Response getAllLocations(@QueryParam("page") int page,
                                    @QueryParam("size") int size) {
        List<Location> locations = locationService.getAllLocations(page, size);
        return Response.ok(locations).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateLocation(@PathParam("id") Long id, @Valid LocationRequest updatedLocation) {
        Location location = locationService.updateLocation(id, updatedLocation);
        return Response.ok(location).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLocation(@PathParam("id") Long id) {
        locationService.deleteLocation(id);
        return Response.noContent().build();
    }
}