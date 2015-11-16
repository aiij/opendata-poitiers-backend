package com.serli.open.data.poitiers.jobs.parsing.disabled.parking;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chris on 14/11/15.
 */
public class DisabledParkingJsonObject {
    public String type;
    public JsonFromFileProperties properties;
    public JsonFromFileGeometry geometry;

    public static class JsonFromFileProperties {
        @JsonProperty("gml_id")
        public String gmlId;
        @JsonProperty("OBJECTID")
        public int objectId;
        @JsonProperty("Identifiant")
        public String identifier;
        @JsonProperty("QUARTIER")
        public String district;
        @JsonProperty("CLASSEMENT")
        public String state;
        @JsonProperty("Observation")
        public String comment;
        @JsonProperty("Commune")
        public String town;
    }

    public static class JsonFromFileGeometry {
        public String type;
        public double[] coordinates;
    }
}
