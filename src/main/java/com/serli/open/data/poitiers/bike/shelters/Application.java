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

        new WebServer().configure(routes -> {
            routes.add(new ShelterEndPoint());
        }).start();
    }
}
