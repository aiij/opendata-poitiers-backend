package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Gets;
import net.codestory.http.annotations.Prefix;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
@Prefix("settings")
public class SettingsEndPoint {
    @Get("dashboard-url")
    public String dashBoardURL() {
        return SettingsRepository.INSTANCE.getDashBoardURL();
    }

    @Gets({@Get(""), @Get("/")})
    public Settings getSettings(){
        return SettingsRepository.INSTANCE.getAllSettings();
    }
}
