package com.serli.open.data.poitiers.jobs;

import com.serli.open.data.poitiers.api.model.DisabledParking;
import com.serli.open.data.poitiers.jobs.parsing.disabled.parking.DisabledParkingJsonObject;
import com.serli.open.data.poitiers.jobs.parsing.disabled.parking.FullDisabledParkingJsonFile;
import com.serli.open.data.poitiers.repository.ElasticRepository;

import java.util.Arrays;

import static com.serli.open.data.poitiers.repository.ElasticRepository.BIKE_SHELTERS_TYPE;
import static com.serli.open.data.poitiers.repository.ElasticRepository.DISABLED_PARKING_TYPE;

/**
 * Created by chris on 14/11/15.
 */
public class ImportDisabledParkingsDataJob extends ImportDataJob<FullDisabledParkingJsonFile> {
    public static void main(String[] args) {
        new ImportDisabledParkingsDataJob().createIndexAndLoad();
    }

    @Override
    protected void indexRootElement(FullDisabledParkingJsonFile rootElement) {
        Arrays.stream(rootElement.disableParkings)
                .forEach(disableParkingJsonObject -> indexDisabledParking(disableParkingJsonObject));
    }

    private void indexDisabledParking(DisabledParkingJsonObject disableParkingJsonObject) {
        DisabledParking disabledParking = new DisabledParking(
                disableParkingJsonObject.properties.objectId,
                disableParkingJsonObject.properties.identifier,
                disableParkingJsonObject.properties.district,
                disableParkingJsonObject.properties.state,
                disableParkingJsonObject.properties.comment,
                disableParkingJsonObject.properties.town,
                disableParkingJsonObject.geometry.coordinates);
        System.out.println(disabledParking);
        ElasticRepository.INSTANCE.index(disabledParking);
    }

    @Override
    protected String fileURLPropertyName() {
        return "disabled.parking.open.data.url";
    }

    @Override
    protected String mappingFilePath() {
        return "/elasticsearch/mappings/disabled-parking.json";
    }

    protected String getElasticType() {
        return DISABLED_PARKING_TYPE;
    }
}
