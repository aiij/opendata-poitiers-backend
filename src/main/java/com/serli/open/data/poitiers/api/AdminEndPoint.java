package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.jobs.Job;
import com.serli.open.data.poitiers.jobs.JobRunner;
import com.serli.open.data.poitiers.jobs.settings.ReloadDefaultSettings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;

/**
 * Created by chris on 13/11/15.
 */
@Prefix("admin")
public class AdminEndPoint {
    @Get("/reload/:type")
    public void reloadData(String type) {
        System.out.println("Reload : " + type);
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        if ("all".equals(type)) {
            settings.sources.values().forEach((source) -> launchJob(source.reloadJobClass));
        } else {
            String reloadJobClass = settings.sources.get(type).reloadJobClass;
            launchJob(reloadJobClass);
        }
    }

    private void launchJob(String reloadJobClass) {
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(reloadJobClass);
            JobRunner.run(jobClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Get("/reload-default-settings")
    public void reloadSettings() {
        new ReloadDefaultSettings().run();
    }


    @Put("/settings/dashboard-url")
    public void putDashboardURL(String newURL) {
        SettingsRepository.INSTANCE.putNewDashBoardURL(newURL);
    }

}
