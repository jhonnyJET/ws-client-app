package domain.api;

import domain.route.RouteService;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class RouteApi {

    RouteService routeService;

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public RouteApi(RouteService routeService) {
        this.routeService = routeService;
    }


    public void kickOffWsConnectBatch(List<String> userIds) throws IOException, InterruptedException {
        Logger.getAnonymousLogger().log(Level.WARNING, "Kick off WS connect batch");
        userIds.forEach(user -> {
            executor.submit(() -> {
                try {
                    routeService.kickoffWsConnection(user);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    public void kickOffWsDisconnectBatch(List<String> userIds) throws IOException, InterruptedException {
        userIds.forEach(userId -> {
                try {
                    routeService.kickoffWsDisconnection(userId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
