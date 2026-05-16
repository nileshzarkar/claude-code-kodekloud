package com.auth.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class H2ConsoleStarter {

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String jdbcUrl;

    void onStart(@Observes StartupEvent event) {
        try {
            org.h2.tools.Server.createWebServer(
                    "-web", "-webAllowOthers", "-webPort", "8082"
            ).start();

            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println(  "║  H2 Web Console  →  http://localhost:8082            ║");
            System.out.println(  "║  JDBC URL        →  " + jdbcUrl);
            System.out.println(  "║  Username: sa    |  Password: (empty)               ║");
            System.out.println(  "╚═══════════════════════════════════════════════════════╝\n");
        } catch (Exception e) {
            System.err.println("H2 Console could not start: " + e.getMessage());
        }
    }
}
