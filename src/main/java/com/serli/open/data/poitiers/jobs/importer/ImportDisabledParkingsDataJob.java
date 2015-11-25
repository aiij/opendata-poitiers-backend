package com.serli.open.data.poitiers.jobs.importer;

import com.serli.open.data.poitiers.api.v2.model.DisabledParking;
import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.geolocation.Address;
import com.serli.open.data.poitiers.geolocation.GeolocationAPIClient;
import com.serli.open.data.poitiers.geolocation.LatLon;
import com.serli.open.data.poitiers.jobs.importer.parsing.disabled.parking.DisabledParkingJsonObject;
import com.serli.open.data.poitiers.jobs.importer.parsing.disabled.parking.FullDisabledParkingJsonFile;
import com.serli.open.data.poitiers.repository.OpenDataRepository;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.util.Arrays;

import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import static com.serli.open.data.poitiers.repository.OpenDataRepository.DISABLED_PARKINGS_TYPE;

/**
 * Created by chris on 14/11/15.
 */
public class ImportDisabledParkingsDataJob extends ImportDataJob<FullDisabledParkingJsonFile> {
    public static void main(String[] args) {
        new ImportDisabledParkingsDataJob().createIndexAndLoad();
    }

    @Override
    protected void indexRootElement(FullDisabledParkingJsonFile rootElement) {
        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(OPEN_DATA_POITIERS_INDEX).defaultType(OpenDataRepository.INSTANCE.getElasticType(DisabledParking.class));

        Arrays.stream(rootElement.disableParkings)
                .forEach(disableParkingJsonObject -> bulk.addAction(getAction(disableParkingJsonObject)));

        ElasticUtils.createClient().execute(bulk.build());

    }

    private Index getAction(DisabledParkingJsonObject disableParkingJsonObject) {
        double[] coordinates = disableParkingJsonObject.geometry.coordinates;
        Address address = GeolocationAPIClient.reverseGeoCode(new LatLon(coordinates[1], coordinates[0]));
        String stringAddress = address.street+ ", " + address.zipCode + " " + address.city;

        DisabledParking disabledParking = new DisabledParking(
                disableParkingJsonObject.properties.objectId,
                disableParkingJsonObject.properties.identifier,
                disableParkingJsonObject.properties.district,
                disableParkingJsonObject.properties.state,
                disableParkingJsonObject.properties.comment,
                disableParkingJsonObject.properties.town,
                stringAddress,
                disableParkingJsonObject.geometry.coordinates);

        return new Index.Builder(disabledParking).build();
    }

    @Override
    protected String getElasticType() {
        return DISABLED_PARKINGS_TYPE;
    }
}
