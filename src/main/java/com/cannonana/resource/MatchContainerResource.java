package com.cannonana.resource;

import com.cannonana.dto.CreateMatchContainerResponse;
import io.quarkus.security.User;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Path("/match-container")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchContainerResource {

    private static final String IMAGE_NAME = "cannonana/aob-match";
    private static final int CONTAINER_PORT = 8080;

    public CreateMatchContainerResponse createMatch(String matchId, List<String> players) {
        try {

            // docker run -d --name ...
            List<String> command = List.of(
                    "docker", "run", "-d",
                    "--name", matchId,
                    "-e", "MATCH_ID=" + matchId,
                    "-e", "PLAYERS=" + String.join(",", players),
                    "-p", "0:" + CONTAINER_PORT,
                    IMAGE_NAME
            );

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String containerId = reader.readLine();
            int exitCode = process.waitFor();

            if (exitCode != 0 || containerId == null || containerId.isBlank()) {
                throw new RuntimeException("Erro ao criar container");
            }

            int hostPort = discoverHostPort(matchId);

            CreateMatchContainerResponse cmcr = CreateMatchContainerResponse.builder().matchId(matchId).port(hostPort).build();

            return cmcr;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Descobre a porta aleatória atribuída pelo Docker
     */
    private int discoverHostPort(String containerName) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(
                "docker", "port", containerName
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        String line = reader.readLine();
        process.waitFor();

        if (line == null || !line.contains(":")) {
            throw new RuntimeException("Não foi possível descobrir a porta do container");
        }

        // Ex: 8080/tcp -> 0.0.0.0:49153
        String port = line.substring(line.lastIndexOf(":") + 1);

        return Integer.parseInt(port.trim());
    }

    @GET
    @Path("/{playerId}")
    public Response getMatchByPlayerId(@PathParam("playerId") String playerId) {
        // Implement logic to retrieve match information by player ID
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"Not implemented yet\"}")
                .build();
    }
}
