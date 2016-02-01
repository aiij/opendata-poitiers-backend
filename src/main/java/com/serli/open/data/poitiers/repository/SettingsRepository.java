package com.serli.open.data.poitiers.repository;

import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
public class SettingsRepository extends ElasticSearchRepository {
    private static final String DASHBOARD_URL_ID = "DASHBOARD_URL_ID";
    private static final String SETTINGS_ID = "settings-id";

    private static final String SETTINGS_TYPE = "settings";

    public static final SettingsRepository INSTANCE = new SettingsRepository();

    public String getDashBoardURL() {
//        Get getQuery = new Get.Builder(OPEN_DATA_POITIERS_INDEX, DASHBOARD_URL_ID).type(SETTINGS_TYPE).build();
//
//        DocumentResult result = client.execute(getQuery);
//
//        if (result.getResponseCode() != 200){
//            return null;
//        }
//        return result.getJsonObject().get("_source").getAsJsonObject().get("url").getAsString();
        return getAllSettings().dashboardURL;
    }

    public void putNewDashBoardURL(String newURL) {
        Settings allSettings = getAllSettings();

        allSettings.dashboardURL = newURL;
        updateSettings(allSettings);
    }

    public void updateSettings(Settings settings){
        Index indexQuery = new Index.Builder(settings).index(OPEN_DATA_POITIERS_INDEX).type(SETTINGS_TYPE).id(SETTINGS_ID).build();

        client.execute(indexQuery);
    }

    public Settings getAllSettings() {
        Get getQuery = new Get.Builder(OPEN_DATA_POITIERS_INDEX, SETTINGS_ID).type(SETTINGS_TYPE).build();

        DocumentResult result = client.execute(getQuery);

        if (result.getResponseCode() != 200){
            return new Settings();
        }
        return result.getSourceAsObject(Settings.class);
    }
}