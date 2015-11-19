/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs;

import com.serli.open.data.poitiers.api.model.Defibrillator;
import com.serli.open.data.poitiers.geolocation.Address;
import com.serli.open.data.poitiers.geolocation.GeolocationAPIClient;
import com.serli.open.data.poitiers.geolocation.LatLon;
import com.serli.open.data.poitiers.jobs.parsing.defibrillator.DefibrillatorJsonObject;
import com.serli.open.data.poitiers.jobs.parsing.defibrillator.FullDefibrillatorJsonFile;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import static com.serli.open.data.poitiers.repository.ElasticRepository.DEFIBRILLATOR_TYPE;
import java.io.IOException;
import java.util.Arrays;
import static org.apache.commons.lang3.StringUtils.capitalize;
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
        DefibrillatorJsonObject[] jsonDefibrillatorFromFiles = fullDefibrillatorJsonFile.defibrillators;
        Arrays.stream(jsonDefibrillatorFromFiles)
                .forEach(jsonFromFile -> indexElement(jsonFromFile));
    }

    private static void indexElement(DefibrillatorJsonObject jsonDefibrillatorFromFile) {
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
        ElasticRepository.INSTANCE.index(defibrillator);
    }

    @Override
    protected String fileURLPropertyName() {
        return "defibrillator.open.data.url";
    }

    @Override
    protected String mappingFilePath() {
        return "/elasticsearch/mappings/defibrillators.json";
    }

    @Override
    protected String getElasticType() {
        return DEFIBRILLATOR_TYPE;
    }
    
}
