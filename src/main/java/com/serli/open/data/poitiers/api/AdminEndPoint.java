package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import static com.serli.open.data.poitiers.jobs.JobRunner.run;
import com.serli.open.data.poitiers.jobs.configuration.GenerateConfigurationFiles;
import com.serli.open.data.poitiers.jobs.importer.v2.ImportDataFromType;
import com.serli.open.data.poitiers.jobs.settings.ReloadDefaultSettings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import org.json.JSONException;

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
            for (Entry<String, DataSource> entry : settings.sources.entrySet())
            {
                ImportDataFromType.elasticType = entry.getKey();
                run(ImportDataFromType.class);
            }
        } else {
            ImportDataFromType.elasticType = type;
            run(ImportDataFromType.class);
        }
    }

    @Put("/reload-default-settings")
    public void reloadSettings() {
        new ReloadDefaultSettings().run();
    }


    @Put("/settings/dashboard-url")
    public void putDashboardURL(String newURL) {
        SettingsRepository.INSTANCE.putNewDashBoardURL(newURL);
    }
    
    @Put("/create-files")
    public void addData(String monJson) {
        try {
            GenerateConfigurationFiles.generateConfFile(monJson);
            GenerateConfigurationFiles.updateDefaultSettings(monJson);
            GenerateConfigurationFiles.generateESMapping(monJson);
            
        } catch (JSONException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }   
}
