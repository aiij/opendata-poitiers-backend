/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer.parsing.data;

import com.serli.open.data.poitiers.jobs.importer.parsing.*;
import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.createMapping;
import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.getElasticSearchURL;
import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;



/**
 *
 * @author Julien L
 */
public class MappingClass {

    //Stores parsing from a config file
    public Map<String, Object> data = new HashMap<> ();
    
    public MappingClass(String filename) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            String path = filename;
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/" + path);
            InputStream ips = new FileInputStream(file.getAbsolutePath());
            //InputStreamReader ipsr = new InputStreamReader(ips, "UTF-8");
            //input = String.class.getResourceAsStream(System.getProperty("user.dir")+ "/src/main/resources/" + path);

            //Load the properties file
            prop.load(ips);
            
            //Full map from config file
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                    Object value = prop.getProperty(key);
                    this.data.put(key, value);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public MappingClass() {
    }
    
}
