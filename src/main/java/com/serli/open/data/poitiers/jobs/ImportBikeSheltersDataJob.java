package com.serli.open.data.poitiers.jobs;

import com.serli.open.data.poitiers.geolocation.Address;
import com.serli.open.data.poitiers.geolocation.GeolocationAPIClient;
import com.serli.open.data.poitiers.geolocation.LatLon;
import com.serli.open.data.poitiers.jobs.parsing.bike.shelters.FullShelterJsonFile;
import com.serli.open.data.poitiers.jobs.parsing.bike.shelters.ShelterJsonObject;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import com.serli.open.data.poitiers.api.model.Shelter;

import java.io.IOException;
import java.util.Arrays;

import static com.serli.open.data.poitiers.repository.ElasticRepository.BIKE_SHELTERS_TYPE;

/**
 * Created by chris on 04/05/15.
 */
public class ImportBikeSheltersDataJob extends ImportDataJob<FullShelterJsonFile> {
    public static void main(String[] args) throws IOException {
       new ImportBikeSheltersDataJob().createIndexAndLoad();
    }

    @Override
    protected void indexRootElement(FullShelterJsonFile fullShelterJsonFile) {
        ShelterJsonObject[] jsonShelterFromFiles = fullShelterJsonFile.shelters;
        Arrays.stream(jsonShelterFromFiles)
                .forEach(jsonFromFile -> indexElement(jsonFromFile));
    }

    private static void indexElement(ShelterJsonObject jsonShelterFromFile) {
        double[] coordinates = jsonShelterFromFile.geometry.coordinates;
        Address address = GeolocationAPIClient.reverseGeoCode(new LatLon(coordinates[1], coordinates[0]));
        String stringAddress = address.street+ ", " + address.zipCode + " " + address.zipCode;

        Shelter shelter = new Shelter(
                jsonShelterFromFile.properties.shelterType,
                jsonShelterFromFile.properties.capacity,
                coordinates,
                jsonShelterFromFile.properties.objectId,
                stringAddress);
        ElasticRepository.INSTANCE.index(shelter);
    }

    @Override
    protected String fileURLPropertyName() {
        return "bike.shelters.open.data.url";
    }

    @Override
    protected String mappingFilePath() {
        return "/elasticsearch/mappings/bike-shelters.json";
    }

    @Override
    protected String getElasticType() {
        return BIKE_SHELTERS_TYPE;
    }
}
