package com.serli.open.data.poitiers.jobs.parsing.bike.shelters;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by chris on 04/05/15.
 */
public class ShelterJsonObject {
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
        @JsonProperty("ETAT")
        public String state;
        @JsonProperty("DATE")
        public Date date;
        @JsonProperty("EQUIPEMENT_GP")
        public String equipementGP;
        @JsonProperty("CODE_SITE")
        public String codeSite;
    }

    public static class JsonFromFileGeometry {
        public String type;
        public double[] coordinates;
    }
}
