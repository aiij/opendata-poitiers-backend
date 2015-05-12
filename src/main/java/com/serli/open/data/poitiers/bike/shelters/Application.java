package com.serli.open.data.poitiers.bike.shelters;

import com.serli.open.data.poitiers.bike.shelters.jobs.InitDataJob;
import com.serli.open.data.poitiers.bike.shelters.rest.ShelterEndPoint;
import net.codestory.http.WebServer;

import java.io.IOException;

/**
 * Created by chris on 04/05/15.
 */
public class Application {
    public static void main(String[] args) throws IOException {
        InitDataJob.loadData();


        WebServer webServer = new WebServer();
        webServer.configure(routes -> {
            routes.get("/", "GET <a href=\"bike-shelters/\">bike-shelters</a> : bike shelters in Poitiers");
            routes.add(new ShelterEndPoint());
        });

        String providedPort = System.getenv("PORT");
        String port = providedPort == null ? "8080" : providedPort;
        webServer.start(Integer.valueOf(port));
    }
}
