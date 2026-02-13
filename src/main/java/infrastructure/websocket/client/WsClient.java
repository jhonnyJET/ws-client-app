package infrastructure.websocket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;
import domain.helpers.Configuration;
import jakarta.inject.Inject;
import jakarta.websocket.ClientEndpoint;
import org.jboss.logging.Logger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@ClientEndpoint()
public class WsClient {
    WebSocketFactory factory;

    @Inject
    Configuration appConfig;

    @Inject
    ConnectionManager connectionManager;

    private static Logger logger = Logger.getLogger(WsClient.class.getName());
    private static final String WS_URI_FORMAT = "ws://%s:%s/api/v1/websocket/%s";

    private final ObjectMapper objectMapper;    

    public WsClient(ObjectMapper objectMapper) {
        this.factory = new WebSocketFactory();
        this.objectMapper = objectMapper;
    }

    public void connect(String user) {
        try {
            URI uri = new URI(String.format(WS_URI_FORMAT, appConfig.host(), appConfig.port(), user));
            WebSocket ws = factory.createSocket(uri.toString());
            ws.addListener(
            new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) {
                    System.out.println("Received: " + message);
                }

                @Override
                public void onError(WebSocket websocket, WebSocketException cause) {
                    System.out.println("Error: " + cause.getMessage());
                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                    System.out.println("Connected to WebSocket server at: " + websocket.getURI() + " with user: " + user);
                    connectionManager.add(user, websocket);
                }

                @Override
                public void onStateChanged(WebSocket websocket, WebSocketState newState) {
                }

                @Override
                public void onDisconnected(WebSocket websocket, 
                                        WebSocketFrame serverCloseFrame, 
                                        WebSocketFrame clientCloseFrame, 
                                        boolean closedByServer) {
                System.out.println("Disconnected. Attempting to reconnect user..." + user + " Close reason: " + serverCloseFrame.getCloseReason() + " - " + serverCloseFrame.getCloseCode());

                // RECONNECTION LOGIC
                connectionManager.getConnection(user).ifPresent(ws -> 
                    reconnect(ws)
                );                                
            }
        });

        try {
            ws.connect();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to WebSocket server", e);
        }

        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI format", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect(String user) {
        connectionManager.getConnection(user).ifPresent(connection -> {
            try {
                connectionManager.remove(user); //First remove from connection manager to prevent reconnection attempts
                connection.sendClose();
                connection.disconnect();
            } catch (Exception e) {
                logger.error("Failed to disconnect WebSocket for user: " + user, e);
            }
        });
    }

    public void reconnect(WebSocket webSocket) {
        try {
            Thread.sleep(5000); // Wait 5s before reconnecting
            logger.warn("Reconnecting to: " + webSocket.getURI());
            webSocket = webSocket.recreate(100).connect();
        } catch (Exception e) {
            logger.error("Reconnection attempt failed", e);
            throw new RuntimeException("Reconnection failed", e);
        }
    }
}
