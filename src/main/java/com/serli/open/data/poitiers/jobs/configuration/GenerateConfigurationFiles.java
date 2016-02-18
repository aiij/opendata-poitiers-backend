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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Julien L
 */
public class GenerateConfigurationFiles {
    
    public static void generateConfFile(String JsonFile) throws JSONException, IOException {
        JSONObject obj = new JSONObject(JsonFile);
        JSONObject properties = obj.getJSONObject("properties");
        
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        Map<String, Object> parsing = new HashMap<>();
        
        for(int i=0; i<properties.length() - 1; i++){
            parsing.put(properties.getJSONObject(""+i).getString("champES"), properties.getJSONObject(""+i).getString("champJson"));
        }
        parsing.put("location", "location");
        settings.conf.put(obj.getString("type"), parsing);
        
        SettingsRepository.INSTANCE.updateSettings(settings);
        /*JSONObject obj = new JSONObject(fileJson);
        JSONObject properties = obj.getJSONObject("properties");
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(System.getProperty("user.dir")+"/src/main/resources/conf/"+obj.getString("type")+".properties")));
        for(int i=0; i<properties.length() - 1; i++){
            writer.write(properties.getJSONObject(""+i).getString("champES")+" = "+properties.getJSONObject(""+i).getString("champJson")+"\n");
        }
        //Mandatory to add coordinates during the indexing
        writer.write("location = location");
        writer.close();*/
    }
    
    /* Modification of default settings */
    public static void updateDefaultSettings(String JsonFile) throws JSONException {
        
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        
        JSONObject objFile = new JSONObject(JsonFile);
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
            
        /*File f = new File(System.getProperty("user.dir")+"/src/main/resources/default.settings/default-settings.json");
        String fichier = f.getAbsolutePath();
        JSONObject objFile = new JSONObject(fileJson);
        String content = "";
        String type = objFile.getString("type");
        String url = objFile.getString("url");
        
        try{
            InputStream ips = new FileInputStream(fichier);
            InputStreamReader ipsr = new InputStreamReader(ips, "UTF-8");
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne=br.readLine())!=null){
               content += ligne + "\n";
            }
            br.close();

            JSONObject obj = new JSONObject(content);
            
            //Adding of routes
            JSONArray routes = obj.getJSONArray("routes");
            
            JSONObject set_object = new JSONObject();
            set_object.put("verb", "GET");
            set_object.put("route", "/api/v2/" + type + "/all");
            set_object.put("testRoute", "/api/v2/" + type + "/all");
            set_object.put("description", "get all "+ type);
            routes.put(routes.length(),set_object);
            
            JSONObject set_find_object = new JSONObject();
            set_find_object.put("verb", "GET");
            set_find_object.put("route", "/api/v2/" + type + "/find?lat=:lat&lon=:lon&size=:size");
            set_find_object.put("testRoute", "/api/v2/" + type + "/find?lat=46.578636&lon=0.337959");
            set_find_object.put("description", "search closest " + type + " from lat/lon point, size is optional");
            routes.put(routes.length(),set_find_object);

            //Adding of sources
            JSONObject sources = obj.getJSONObject("sources");
            JSONObject ens_source_add = new JSONObject();
            ens_source_add.put("reloadDataURL", "/admin/reload/" + type);
            ens_source_add.put("openDataFileURL", url);
            ens_source_add.put("mappingFilePath", "/elasticsearch/mappings/" + type + ".json");
            ens_source_add.put("configFile", "conf/" + type + ".properties");
            sources.put(type, ens_source_add);

            //Writing the default settings
            JSONObject defaultSettings = new JSONObject();
            defaultSettings.put("dashboardURL", obj.getString("dashboardURL"));
            defaultSettings.put("routes", routes);
            defaultSettings.put("sources", sources);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+"/src/main/resources/default.settings/default-settings.json"), "UTF-8"));
            
            writer.write(defaultSettings.toString(2));
            writer.close();
        }    
        catch (Exception e){
           System.out.println(e.toString());
        }*/
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
        
        /*JSONObject json;
        
        json = new JSONObject(jsonFile);
        String type = json.getString("type");
        JSONObject properties = json.getJSONObject("properties");
        
        try {
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/elasticsearch/mappings/" +type + ".json");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            
            //Writing of mapping 
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir")+ "/src/main/resources/elasticsearch/mappings/" +type + ".json"), "UTF-8"));
            bw.write("{\n" +
                    "   \"" + type + "\" : {\n" +
                    "       \"properties\" : {\n");
            for (int i = 0; i <= properties.length(); i++) {
                try {                    
                    if(!properties.getJSONObject("" + i + "").getString("champES").equals("location")) {
                        //Only "not-analyzed" fields
                        if(properties.getJSONObject("" + i + "").getString("mapping").equals("true")) {
                            bw.write("          \"" + properties.getJSONObject("" + i + "").getString("champES") + "\" : { \n" +
                                    "               \"type\" : \"string\", \n" +
                                    "               \"index\" : \"not_analyzed\" \n" +
                                    "           }, \n");
                        }
                    } else {
                        //Location is always a "geo-point"
                        bw.write("          \"location\" : { \n" +
                                "               \"type\" : \"geo_point\"\n" +
                                "           } \n" +
                                "       } \n" +
                                "   } \n" +
                                "} \n");
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(GenerateConfigurationFiles.class.getName()).log(Level.SEVERE, null, ex);
                }
            }    
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(GenerateConfigurationFiles.class.getName()).log(Level.SEVERE, null, ex);
        }*/   
    }  
}
