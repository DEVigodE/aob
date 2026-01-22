package com.cannonana.resource;

import java.util.List;
import java.util.Optional;

import com.cannonana.entity.Partida;
import com.cannonana.service.PartidaService;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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

@Path("/partidas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PartidaResource {

    @Inject
    PartidaService partidaService;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @PermitAll
    public List<Partida> listarTodas() {
        return partidaService.listarTodas();
    }

    @GET
    @Path("/{id}")
    @PermitAll
    public Response buscarPorId(@PathParam("id") Long id) {
        Optional<Partida> partida = partidaService.buscarPorId(id);

        if (partida.isPresent()) {
            return Response.ok(partida.get()).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"Partida não encontrada\"}")
                .build();
    }

    @POST
    @RolesAllowed("admin")
    public Response criar(@Valid Partida partida) {
        Partida partidaCriada = partidaService.criar(partida);
        return Response.status(Response.Status.CREATED).entity(partidaCriada).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response atualizar(@PathParam("id") Long id, @Valid Partida partida) {
        Optional<Partida> partidaAtualizada = partidaService.atualizar(id, partida);

        if (partidaAtualizada.isPresent()) {
            return Response.ok(partidaAtualizada.get()).build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"Partida não encontrada\"}")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response deletar(@PathParam("id") Long id) {
        boolean deletado = partidaService.deletar(id);

        if (deletado) {
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"Partida não encontrada\"}")
                .build();
    }

    @GET
    @Path("/me")
    @RolesAllowed({ "user", "admin" })
    public Response getUserInfo() {
        return Response.ok()
                .entity("{\"username\": \"" + securityIdentity.getPrincipal().getName() +
                        "\", \"roles\": " + securityIdentity.getRoles() + "}")
                .build();
    }
}
