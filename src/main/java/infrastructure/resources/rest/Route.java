package infrastructure.resources.rest;

import domain.api.RouteApi;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/route")
public class Route {

    @Inject
    RouteApi api;

    @POST
    @Path("kickstart/ws/batch/connect")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response kickOffWsConnectBatch(@RequestBody Map<String, List<String>> users) throws IOException, InterruptedException {
        try {
            api.kickOffWsConnectBatch(users.get("userIds"));
        } catch (Exception e) {
            throw e;
        }

        return Response.accepted().build();
    }

    @POST
    @Path("kickstart/ws/batch/disconnect")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response kickOffWsDisconnectBatch(@RequestBody Map<String, List<String>> users) throws IOException, InterruptedException {
        try {
            api.kickOffWsDisconnectBatch(users.get("userIds"));
        } catch (Exception e) {
            throw e;
        }

        return Response.accepted().build();
    }

}
