package com.serli.open.data.poitiers.bike.shelters;

import com.serli.open.data.poitiers.bike.shelters.rest.AdminEndPoint;
import com.serli.open.data.poitiers.bike.shelters.rest.ShelterEndPoint;
import com.serli.open.data.poitiers.utils.EnvUtils;
import net.codestory.http.WebServer;
import net.codestory.http.filters.basic.BasicAuthFilter;
import net.codestory.http.security.UsersList;

import java.io.IOException;

import static com.serli.open.data.poitiers.utils.EnvUtils.getEnvOrDefault;

/**
 * Created by chris on 04/05/15.
 */
public class Application {
    public static void main(String[] args) throws IOException {
//        InitDataJob.loadData();

        BasicAuthFilter filter = createBasicAuthFilter();

        WebServer webServer = new WebServer();
        webServer.configure(routes -> {
            routes.get("/", "GET <a href=\"bike-shelters/\">bike-shelters</a> : bike shelters in Poitiers");
            routes.add(new ShelterEndPoint());
            routes.add(new AdminEndPoint());
            routes.filter(filter);
        });

        String port = getEnvOrDefault("PORT", "8080");
        webServer.start(Integer.valueOf(port));
    }

    private static BasicAuthFilter createBasicAuthFilter() {
        String adminLogin = getEnvOrDefault("ADMIN_LOGIN", "admin");
        String adminPassword = getEnvOrDefault("ADMIN_PASSWORD", "admin");

        UsersList users = new UsersList.Builder()
                .addUser(adminLogin, adminPassword)
                .build();
        return new BasicAuthFilter("/admin", "open.data", users);
    }
}
