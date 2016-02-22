/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer.parsing.data;

import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Julien L
 */
public class MappingClass {

    //Stores parsing from configurations
    public Map<String, Object> data = new HashMap<> ();
    
    public MappingClass(String type) {
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        this.data.putAll(settings.conf.get(type));
    }

    public MappingClass() {
    }
}
