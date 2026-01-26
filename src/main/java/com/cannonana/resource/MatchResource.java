package com.cannonana.resource;

import com.cannonana.dto.CreateMatchContainerResponse;
import com.cannonana.dto.CreateMatchRequest;
import com.cannonana.dto.CreateMatchResponse;
import com.cannonana.entity.Match;
import com.cannonana.service.MatchService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/match")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MatchResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchResource.class);
    @Inject
    MatchContainerResource matchContainerResource;
    @Inject
    MatchService matchService;

    @POST
    @RolesAllowed("user")
    public Response createMatch(CreateMatchRequest createMatchRequest) {
        String matchId = Instant.now().toEpochMilli() + UUID.randomUUID().toString().substring(0, 2);
        LocalDateTime dateTime = LocalDateTime.now(ZoneOffset.UTC);

        CreateMatchContainerResponse response = matchContainerResource.createMatch(matchId, createMatchRequest.users);

        Match match = Match.builder().matchId(matchId).createdAt(dateTime).build();
        matchService.create(match);

        LOGGER.info("Match created with id: {} on port: {} at {}", matchId, response.getPort(), dateTime);

        CreateMatchResponse createMatchResponse = CreateMatchResponse.builder().matchId(matchId).hostPort(response.getPort()).build();
        return Response.status(Response.Status.CREATED).entity(createMatchResponse).build();

    }

}
