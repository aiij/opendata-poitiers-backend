package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.jobs.Job;
import com.serli.open.data.poitiers.jobs.JobRunner;
import static com.serli.open.data.poitiers.jobs.JobRunner.run;
import com.serli.open.data.poitiers.jobs.configuration.GenerateConfigurationFiles;
import com.serli.open.data.poitiers.jobs.importer.ImportAllDataJob;
import com.serli.open.data.poitiers.jobs.settings.ReloadDefaultSettings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                ImportAllDataJob.elasticType = entry.getKey();
                run(ImportAllDataJob.class);
            }
        } else {
            ImportAllDataJob.elasticType = type;
            run(ImportAllDataJob.class);
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
            
        } catch (JSONException ex) {
            Logger.getLogger(AdminEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdminEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
