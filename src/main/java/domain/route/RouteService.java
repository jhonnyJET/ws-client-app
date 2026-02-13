package domain.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.external.WsServer;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RouteService {

    private final ObjectMapper objectMapper;
    WsServer wsServer;

    public RouteService(ObjectMapper objectMapper,
                        WsServer wsServer) {
        this.objectMapper = objectMapper;
        this.wsServer = wsServer;
    }

    public void kickoffWsConnection(String user){
        wsServer.connect(user);
    }

    public void kickoffWsDisconnection(String user){
        wsServer.disconnect(user);
    }
}
