/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer;

import com.serli.open.data.poitiers.api.v2.model.GlassContainer;
import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.jobs.importer.parsing.glass.container.FullGlassContainerJsonFile;
import com.serli.open.data.poitiers.jobs.importer.parsing.glass.container.GlassContainerJsonObject;
import com.serli.open.data.poitiers.repository.OpenDataRepository;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import static com.serli.open.data.poitiers.repository.OpenDataRepository.GLASS_CONTAINERS_TYPE;
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

        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(OPEN_DATA_POITIERS_INDEX).defaultType(OpenDataRepository.INSTANCE.getElasticType(GlassContainer.class));

        Arrays.stream(jsonGlassContainerFromFiles)
                .forEach(jsonFromFile -> bulk.addAction(getAction(jsonFromFile)));

        ElasticUtils.createClient().execute(bulk.build());

    }
    
    private static Index getAction(GlassContainerJsonObject jsonGlassContainerFromFile) {

        GlassContainer glassContainer = new GlassContainer(
                jsonGlassContainerFromFile.properties.numBorne,
                jsonGlassContainerFromFile.properties.volume,
                jsonGlassContainerFromFile.properties.freqCollect,
                jsonGlassContainerFromFile.properties.observation,
                jsonGlassContainerFromFile.properties.jourCollect,
                jsonGlassContainerFromFile.geometry.coordinates,
                jsonGlassContainerFromFile.properties.objectId,
                jsonGlassContainerFromFile.properties.adresse);
        return new Index.Builder(glassContainer).build();
    }

    @Override
    protected String getElasticType() {
        return GLASS_CONTAINERS_TYPE;
    }
    
}
