package com.serli.open.data.poitiers.jobs.importer.v1;

import com.serli.open.data.poitiers.api.v2.model.Shelter;
import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.geolocation.Address;
import com.serli.open.data.poitiers.geolocation.GeolocationAPIClient;
import com.serli.open.data.poitiers.geolocation.LatLon;
import com.serli.open.data.poitiers.jobs.importer.parsing.bike.shelters.FullShelterJsonFile;
import com.serli.open.data.poitiers.jobs.importer.parsing.bike.shelters.ShelterJsonObject;
import com.serli.open.data.poitiers.repository.OpenDataRepository;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.io.IOException;
import java.util.Arrays;

import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import static com.serli.open.data.poitiers.repository.OpenDataRepository.BIKE_SHELTERS_TYPE;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.lowerCase;

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

        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(OPEN_DATA_POITIERS_INDEX).defaultType(getElasticType());

        Arrays.stream(jsonShelterFromFiles)
                .forEach(jsonFromFile -> bulk.addAction(getAction(jsonFromFile)));

        ElasticUtils.createClient().execute(bulk.build());
    }

    private Index getAction(ShelterJsonObject jsonShelterFromFile) {
        double[] coordinates = jsonShelterFromFile.geometry.coordinates;
        Address address = GeolocationAPIClient.reverseGeoCode(new LatLon(coordinates[1], coordinates[0]));
        String stringAddress = address.street+ ", " + address.zipCode + " " + address.city;

        Shelter shelter = new Shelter(
                capitalize(lowerCase(jsonShelterFromFile.properties.shelterType)),
                jsonShelterFromFile.properties.capacity,
                coordinates,
                jsonShelterFromFile.properties.objectId,
                stringAddress);
        return new Index.Builder(shelter).build();
    }

    @Override
    protected String getElasticType() {
        return BIKE_SHELTERS_TYPE;
    }
}
