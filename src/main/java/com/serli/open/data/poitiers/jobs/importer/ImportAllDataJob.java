/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer;

import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.geolocation.Address;
import com.serli.open.data.poitiers.geolocation.GeolocationAPIClient;
import com.serli.open.data.poitiers.geolocation.LatLon;
import com.serli.open.data.poitiers.jobs.importer.parsing.data.DataJsonObject;
import com.serli.open.data.poitiers.jobs.importer.parsing.data.FullDataJsonFile;
import com.serli.open.data.poitiers.jobs.importer.parsing.data.MappingClass;
import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Julien L
 */
public class ImportAllDataJob extends ImportDataJob<FullDataJsonFile> {

    public void main(String[] args) throws IOException {
        new ImportAllDataJob().createIndexAndLoad();
    }
    
    public static String elasticType;
    //config file
    public static String filename;
    
    @Override
    protected void indexRootElement(FullDataJsonFile fullDataJsonFile) {
        DataJsonObject[] jsonDataFromFiles = fullDataJsonFile.data;

        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(OPEN_DATA_POITIERS_INDEX).defaultType(getElasticType());

        Arrays.stream(jsonDataFromFiles)
                .forEach(jsonFromFile -> bulk.addAction(getAction(jsonFromFile)));
        
        ElasticUtils.createClient().execute(bulk.build());
    }

    private Index getAction(DataJsonObject jsonDataFromFile) {
        
        String path = getFilename();
        MappingClass mappingClass = new MappingClass(path);
        
        for(Map.Entry<String, Object> entry : mappingClass.data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            //Adding of the coordinates
            if("location".equals(value)) {
                    mappingClass.data.put(key, jsonDataFromFile.geometry.coordinates);
            } else {
                //Reverse geocoding
                if("address".equals(value)) {   
                    double[] coordinates = jsonDataFromFile.geometry.coordinates;
                    Address add = GeolocationAPIClient.reverseGeoCode(new LatLon(coordinates[1], coordinates[0]));
                    String stringAddress = add.street+ ", " + add.zipCode + " " + add.city;
                    mappingClass.data.put(key, stringAddress);
                } 
                //Adding of others properties
                else {
                    final Object property = jsonDataFromFile.properties.get(value);
                    mappingClass.data.put(key, property);
                }
            }
        }
        
        return new Index.Builder(mappingClass.data).build();

    }

    @Override
    protected String getElasticType() {
        return elasticType;
    }
    
    private String getFilename() {
        return filename;
    }
}