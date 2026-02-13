package domain.external;

public interface WsServer {
    void connect(String user);
    void disconnect(String user);
}
