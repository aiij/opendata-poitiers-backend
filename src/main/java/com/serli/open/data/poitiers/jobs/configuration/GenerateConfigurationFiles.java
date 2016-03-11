/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.configuration;

import com.serli.open.data.poitiers.api.v2.model.settings.APIRoutes;
import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Julien L
 */
public class GenerateConfigurationFiles {
    
    public static void generateConfFile(String jsonFile) throws JSONException, IOException {
        JSONObject obj = new JSONObject(jsonFile);
        JSONObject properties = obj.getJSONObject("properties");
        
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        Map<String, Object> parsing = new HashMap<>();
        
        for(int i=0; i<properties.length() - 1; i++){
            parsing.put(properties.getJSONObject(""+i).getString("champES"), properties.getJSONObject(""+i).getString("champJson"));
        }
        parsing.put("location", "location");
        settings.conf.put(obj.getString("type"), parsing);
        
        SettingsRepository.INSTANCE.updateSettings(settings);
    }
    
    /* Modification of default settings */
    public static void updateDefaultSettings(String jsonFile) throws JSONException {
        
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        
        JSONObject objFile = new JSONObject(jsonFile);
        String type = objFile.getString("type");
        String url = objFile.getString("url");
        
        //Adding routes
        APIRoutes route_all = new APIRoutes();
        route_all.verb = "GET";
        route_all.route = "/api/v2/" + type + "/all";
        route_all.testRoute = "/api/v2/" + type + "/all";
        route_all.description = "get all "+ type;
        
        APIRoutes route_find = new APIRoutes();
        route_find.verb = "GET";
        route_find.route = "/api/v2/" + type + "/find?lat=:lat&lon=:lon&size=:size";
        route_find.testRoute = "/api/v2/" + type + "/find?lat=46.578636&lon=0.337959";
        route_find.description = "search closest " + type + " from lat/lon point, size is optional";

        settings.routes.add(route_all);
        settings.routes.add(route_find);
        
        //Adding sources
        DataSource data = new DataSource();
        data.reloadDataURL = "/admin/reload/" + type;
        data.openDataFileURL = url;
        settings.sources.put(type, data);

        SettingsRepository.INSTANCE.updateSettings(settings);
        
    }

    /* Generation of Elasticsearch mapping */ 
    public static void generateESMapping(String jsonFile) throws JSONException {
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        JSONObject json = new JSONObject(jsonFile);
        String type = json.getString("type");
        JSONObject properties = json.getJSONObject("properties");
        
        String content;
        
        content =   "{\n" +
                    "   \"" + type + "\" : {\n" +
                    "       \"properties\" : {\n";

        for (int i = 0; i < properties.length(); i++) {                 
            if(!properties.getJSONObject("" + i + "").getString("champES").equals("location")) {
                //Only "not-analyzed" fields
                if(properties.getJSONObject("" + i + "").getString("mapping").equals("true")) {
                    content +=  "          \"" + properties.getJSONObject("" + i + "").getString("champES") + "\" : { \n" +
                                "               \"type\" : \"string\", \n" +
                                "               \"index\" : \"not_analyzed\" \n" +
                                "           }, \n";
                }
            } else {
                //Location is always a "geo-point"
                content += "          \"location\" : { \n" +
                        "               \"type\" : \"geo_point\"\n" +
                        "           } \n" +
                        "       } \n" +
                        "   } \n" +
                        "} \n";
            }
        }

        settings.mapping.put(type, content);
        SettingsRepository.INSTANCE.updateSettings(settings);
    }  
}
