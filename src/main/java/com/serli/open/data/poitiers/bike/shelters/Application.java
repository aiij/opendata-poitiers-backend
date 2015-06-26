package com.serli.open.data.poitiers.bike.shelters;

import com.serli.open.data.poitiers.bike.shelters.jobs.InitDataJob;
import com.serli.open.data.poitiers.bike.shelters.rest.EndPoint;

import net.codestory.http.WebServer;

import java.io.IOException;

/**
 * Created by chris on 04/05/15.
 */
public class Application {
    public static void main(String[] args) throws IOException {
        InitDataJob.loadDataShelters();
        InitDataJob.loadDataTicketmachine();
        InitDataJob.loadDataPark();
        InitDataJob.loadDataService();

        WebServer webServer = new WebServer();
        webServer.configure(routes -> {
            routes.get("/", "GET <a href=\"allInstallations/\">all installations</a> : all installations in Poitiers");
            routes.add(new EndPoint());
        });

        String providedPort = System.getenv("PORT");
        String port = providedPort == null ? "8080" : providedPort;
        webServer.start(Integer.valueOf(port));
    }
}
