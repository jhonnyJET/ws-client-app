package infrastructure.websocket.client;

import com.neovisionaries.ws.client.WebSocket;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ConnectionManager {

    private final Map<String, WebSocket> activeSessions = new ConcurrentHashMap<>();

    public List<String> getSessionMap() {
        return  activeSessions.keySet().stream().toList();
    }

    public void add(String userId, WebSocket socket) {
        activeSessions.put(userId, socket);
    }

    public void remove(String userId) {
        activeSessions.remove(userId);
    }

    public Optional<WebSocket> getConnection(String userId) {
        return Optional.ofNullable(activeSessions.get(userId));
    }

    public int getTotalConnections() {
        return activeSessions.size();
    }
}
