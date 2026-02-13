package domain.helpers;

import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.context.ApplicationScoped;


@ConfigMapping(prefix = "quarkus.ws-server")
@ApplicationScoped
public interface Configuration {
    String host();
    String port();
}
