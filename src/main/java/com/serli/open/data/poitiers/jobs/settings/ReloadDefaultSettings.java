package com.serli.open.data.poitiers.jobs.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.jobs.Job;
import com.serli.open.data.poitiers.repository.SettingsRepository;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
public class ReloadDefaultSettings implements Job {

    public static void main(String[] args) {
        new ReloadDefaultSettings().run();
    }

    @Override
    public void run() {
      
        InputStream inputStream = ReloadDefaultSettings.class.getResourceAsStream("/default.settings/default-settings.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Settings settings;
        try {
            settings = objectMapper.readValue(inputStream, Settings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SettingsRepository.INSTANCE.updateSettings(settings);
    }
}
