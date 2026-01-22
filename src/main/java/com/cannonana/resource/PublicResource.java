package com.cannonana.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/public")
@Produces(MediaType.APPLICATION_JSON)
public class PublicResource {

    @GET
    @Path("/health")
    public String health() {
        return "{\"status\": \"UP\", \"message\": \"API está funcionando - endpoint público\"}";
    }

    @GET
    @Path("/info")
    public String info() {
        return "{\"app\": \"Quarkus + Keycloak\", \"version\": \"1.0.0\", \"realm\": \"aob\"}";
    }
}
