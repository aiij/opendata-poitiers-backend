/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs;

import com.serli.open.data.poitiers.api.model.GlassContainer;
import com.serli.open.data.poitiers.geolocation.Address;
import com.serli.open.data.poitiers.geolocation.GeolocationAPIClient;
import com.serli.open.data.poitiers.geolocation.LatLon;
import com.serli.open.data.poitiers.jobs.parsing.glass.container.FullGlassContainerJsonFile;
import com.serli.open.data.poitiers.jobs.parsing.glass.container.GlassContainerJsonObject;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import static com.serli.open.data.poitiers.repository.ElasticRepository.GLASS_CONTAINER_TYPE;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author dupar_000
 */
public class ImportGlassContainerDataJob extends ImportDataJob<FullGlassContainerJsonFile> {

    public static void main(String[] args) throws IOException {
       new ImportGlassContainerDataJob().createIndexAndLoad();
    }

    @Override
    protected void indexRootElement(FullGlassContainerJsonFile fullGlassContainerJsonFile) {
        GlassContainerJsonObject[] jsonGlassContainerFromFiles = fullGlassContainerJsonFile.GlassContainer;
        Arrays.stream(jsonGlassContainerFromFiles)
                .forEach(jsonFromFile -> indexElement(jsonFromFile));
    }
    
    private static void indexElement(GlassContainerJsonObject jsonGlassContainerFromFile) {

        GlassContainer glassContainer = new GlassContainer(
                jsonGlassContainerFromFile.properties.numBorne,
                jsonGlassContainerFromFile.properties.volume,
                jsonGlassContainerFromFile.properties.freqCollect,
                jsonGlassContainerFromFile.properties.observation,
                jsonGlassContainerFromFile.properties.jourCollect,
                jsonGlassContainerFromFile.geometry.coordinates,
                jsonGlassContainerFromFile.properties.objectId,
                jsonGlassContainerFromFile.properties.adresse);
        ElasticRepository.INSTANCE.index(glassContainer);
    }

    @Override
    protected String fileURLPropertyName() {
         return "glass.container.open.data.url";
    }

    @Override
    protected String mappingFilePath() {
        return "/elasticsearch/mappings/glass-container.json";
    }

    @Override
    protected String getElasticType() {
        return GLASS_CONTAINER_TYPE;
    }
    
}
