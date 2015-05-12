package com.serli.open.data.poitiers.bike.shelters.jobs.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chris on 04/05/15.
 */
public class JsonFromFile {
    public String type;
    public JsonFromFileProperties properties;
    public JsonFromFileGeometry geometry;

    public static class JsonFromFileProperties {
        @JsonProperty("gml_id")
        public String gmlId;
        @JsonProperty("OBJECTID")
        public int objectId;
        @JsonProperty("TYPE_DE_STATIONNEMENT")
        public String shelterType;
        @JsonProperty("NOMBRE")
        public int capacity;
    }

    public static class JsonFromFileGeometry {
        public String type;
        public double[] coordinates;
    }
}
