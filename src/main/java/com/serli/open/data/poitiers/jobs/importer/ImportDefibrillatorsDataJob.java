/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer;

import com.serli.open.data.poitiers.api.v2.model.Defibrillator;
import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.geolocation.Address;
import com.serli.open.data.poitiers.geolocation.GeolocationAPIClient;
import com.serli.open.data.poitiers.geolocation.LatLon;
import com.serli.open.data.poitiers.jobs.importer.parsing.defibrillator.DefibrillatorJsonObject;
import com.serli.open.data.poitiers.jobs.importer.parsing.defibrillator.FullDefibrillatorJsonFile;
import com.serli.open.data.poitiers.repository.OpenDataRepository;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import static com.serli.open.data.poitiers.repository.OpenDataRepository.DEFIBRILLATORS_TYPE;
import java.io.IOException;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.lowerCase;

/**
 *
 * @author Julien L
 */
public class ImportDefibrillatorsDataJob  extends ImportDataJob<FullDefibrillatorJsonFile> {
    public static void main(String[] args) throws IOException {
       new ImportDefibrillatorsDataJob().createIndexAndLoad();
    }

    @Override
    protected void indexRootElement(FullDefibrillatorJsonFile fullDefibrillatorJsonFile) {
        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(OPEN_DATA_POITIERS_INDEX).defaultType(OpenDataRepository.INSTANCE.getElasticType(Defibrillator.class));

        DefibrillatorJsonObject[] jsonDefibrillatorFromFiles = fullDefibrillatorJsonFile.defibrillators;
        Arrays.stream(jsonDefibrillatorFromFiles)
                .forEach(jsonFromFile -> bulk.addAction(getAction(jsonFromFile)));

        ElasticUtils.createClient().execute(bulk.build());
    }

    private Index getAction(DefibrillatorJsonObject jsonDefibrillatorFromFile) {
        double[] coordinates = jsonDefibrillatorFromFile.geometry.coordinates;
        Address address = GeolocationAPIClient.reverseGeoCode(new LatLon(coordinates[1], coordinates[0]));
        String stringAddress = address.street+ ", " + address.zipCode + " " + address.city;

        Defibrillator defibrillator = new Defibrillator(     
                jsonDefibrillatorFromFile.properties.identifier,
                coordinates,
                jsonDefibrillatorFromFile.properties.objectId,
                jsonDefibrillatorFromFile.properties.town,
                jsonDefibrillatorFromFile.properties.place,
                jsonDefibrillatorFromFile.properties.addressNumber,
                stringAddress,
                jsonDefibrillatorFromFile.properties.accessCondition);
        return new Index.Builder(defibrillator).build();
    }

    @Override
    protected String getElasticType() {
        return DEFIBRILLATORS_TYPE;
    }
    
}
