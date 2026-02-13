package infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.websocket.client.WsClient;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WsServer implements domain.external.WsServer {

    private final WsClient wsClient;

    private final ObjectMapper objectMapper;

    public WsServer(WsClient wsTrackerClient, ObjectMapper objectMapper) {
        this.wsClient = wsTrackerClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void connect(String user) {
        wsClient.connect(user);
    }

    @Override
    public void disconnect(String user) {
        wsClient.disconnect(user);
    }
}
