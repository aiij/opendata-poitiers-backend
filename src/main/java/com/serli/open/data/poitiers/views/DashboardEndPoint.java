package com.serli.open.data.poitiers.views;

import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Gets;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.templating.ModelAndView;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
@Prefix("/dashboard")
public class DashboardEndPoint {
    @Gets({@Get(""),@Get("/")})
    public ModelAndView showDashboard(){
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        return ModelAndView.of("_layouts/dashboard","dashboardURL",settings.dashboardURL);
    }
}
