package com.serli.open.data.poitiers;

import com.serli.open.data.poitiers.api.AdminEndPoint;
import com.serli.open.data.poitiers.api.DisableParkingEndPoint;
import com.serli.open.data.poitiers.api.GlassContainerEndPoint;
import com.serli.open.data.poitiers.api.ShelterEndPoint;
import com.serli.open.data.poitiers.elasticsearch.DeveloppementESNode;
import net.codestory.http.WebServer;
import net.codestory.http.filters.basic.BasicAuthFilter;
import net.codestory.http.security.UsersList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.serli.open.data.poitiers.utils.EnvUtils.getEnvOrDefault;

/**
 * Created by chris on 04/05/15.
 */
public class Application {
    private static final String SKIP_CREATE_ES_DEV_NODE = "SKIP_CREATE_ES_DEV_NODE";


    public static void main(String[] args) throws IOException {
        BasicAuthFilter filter = createBasicAuthFilter();

        WebServer webServer = new WebServer();
        webServer.configure(routes -> {
            routes.add(new ShelterEndPoint());
            routes.add(new DisableParkingEndPoint());
            routes.add(new GlassContainerEndPoint());
            routes.add(new AdminEndPoint());
            routes.filter(filter);
        });

        String port = getEnvOrDefault("PORT", "8080");
        webServer.start(Integer.valueOf(port));

        startESInDevMode();
    }

    private static void startESInDevMode() {
        boolean prodMode = Boolean.parseBoolean(System.getProperty("PROD_MODE", "false"));
        boolean skipDevESNode = Boolean.parseBoolean(System.getProperty(SKIP_CREATE_ES_DEV_NODE, "false"));
        if (!prodMode && !skipDevESNode) {
            System.out.println("Start with -D" + SKIP_CREATE_ES_DEV_NODE + "=true To skip ES dev node creation");
            DeveloppementESNode.createDevNode();
        }
    }

    private static BasicAuthFilter createBasicAuthFilter() {
        String adminLogin = getEnvOrDefault("ADMIN_LOGIN", "admin");
        String adminPassword = getEnvOrDefault("ADMIN_PASSWORD", "admin");

        UsersList users = new UsersList.Builder()
                .addUser(adminLogin, adminPassword)
                .build();
        return new BasicAuthFilter("/admin", "open.data.poitiers", users);
    }
}
