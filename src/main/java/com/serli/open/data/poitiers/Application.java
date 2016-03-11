package com.serli.open.data.poitiers;

import com.serli.open.data.poitiers.api.*;
import com.serli.open.data.poitiers.api.v1.ShelterEndPoint;
import com.serli.open.data.poitiers.api.v2.APIEndPoint;
import com.serli.open.data.poitiers.elasticsearch.DeveloppementESNode;
import com.serli.open.data.poitiers.views.DashboardEndPoint;
import net.codestory.http.WebServer;
import net.codestory.http.filters.basic.BasicAuthFilter;
import net.codestory.http.security.UsersList;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.serli.open.data.poitiers.utils.EnvUtils.getEnvOrDefault;

/**
 * This is the entry point of the Application, it is the main class which launchs web server
 * and handles configuration.
 */
public class Application {
    private static final String SKIP_CREATE_ES_DEV_NODE = "SKIP_CREATE_ES_DEV_NODE";


    public static void main(String[] args) throws IOException {

        BasicAuthFilter filter = createBasicAuthFilter();

        WebServer webServer = new WebServer();
        webServer.configure(routes -> {
            routes.add(new ShelterEndPoint());
            routes.add(new AdminEndPoint());
            routes.add(new SettingsEndPoint());
            routes.add(new DashboardEndPoint());
            routes.add(new APIEndPoint());
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
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    DeveloppementESNode.createDevNode();
                }
            });
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