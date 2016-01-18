package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.jobs.Job;
import com.serli.open.data.poitiers.jobs.JobRunner;
import static com.serli.open.data.poitiers.jobs.JobRunner.run;
import com.serli.open.data.poitiers.jobs.importer.ImportAllDataJob;
import com.serli.open.data.poitiers.jobs.settings.ReloadDefaultSettings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import java.util.Map.Entry;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;

/**
 * Created by chris on 13/11/15.
 */
@Prefix("admin")
public class AdminEndPoint {
    @Put("/reload/:type")
    public void reloadData(String type) {
        System.out.println("Reload : " + type);
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        if ("all".equals(type)) {
            //settings.sources.values().forEach((source) -> launchJob(source.reloadJobClass));
            for (Entry<String, DataSource> entry : settings.sources.entrySet())
            {
                ImportAllDataJob.elasticType = entry.getKey();
                ImportAllDataJob.filename = entry.getValue().configFile;
                run(ImportAllDataJob.class);
            }
        } else {
            ImportAllDataJob.elasticType = type;
            ImportAllDataJob.filename = settings.sources.get(type).configFile;
            run(ImportAllDataJob.class);
        }
    }

    /*private void launchJob(String reloadJobClass) {
        try {
            Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(reloadJobClass);
            JobRunner.run(jobClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }*/

    @Put("/reload-default-settings")
    public void reloadSettings() {
        new ReloadDefaultSettings().run();
    }


    @Put("/settings/dashboard-url")
    public void putDashboardURL(String newURL) {
        SettingsRepository.INSTANCE.putNewDashBoardURL(newURL);
    }

}
