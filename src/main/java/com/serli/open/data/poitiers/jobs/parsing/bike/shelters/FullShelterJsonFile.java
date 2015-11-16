package com.serli.open.data.poitiers.jobs.parsing.bike.shelters;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by chris on 14/11/15.
 */
public class FullShelterJsonFile {
    public String type;
    public Map<String,Object> crs;
    @JsonProperty("features")
    public ShelterJsonObject[] shelters;
}
