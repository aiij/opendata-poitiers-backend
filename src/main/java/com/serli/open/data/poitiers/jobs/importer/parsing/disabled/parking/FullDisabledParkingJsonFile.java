package com.serli.open.data.poitiers.jobs.importer.parsing.disabled.parking;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by chris on 14/11/15.
 */
public class FullDisabledParkingJsonFile {
    public String type;
    public Map<String,Object> crs;
    @JsonProperty("features")
    public DisabledParkingJsonObject[] disableParkings;
}
