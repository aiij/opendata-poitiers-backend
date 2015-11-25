/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer.parsing.defibrillator;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Julien L
 */
public class DefibrillatorJsonObject {
    public String type;
    public JsonFromFileProperties properties;
    public JsonFromFileGeometry geometry;

    public static class JsonFromFileProperties {
        @JsonProperty("gml_id")
        public String gmlId;
        @JsonProperty("OBJECTID")
        public int objectId;
        @JsonProperty("Commune")
        public String town;
        @JsonProperty("Localisation")
        public String place;
        @JsonProperty("Identifiant")
        public String identifier;
        @JsonProperty("Num\u00e9ro_adresse")
        public int addressNumber;
        @JsonProperty("Adresse")
        public String address;
        @JsonProperty("Conditions_d_acc\u00e8s")
        public String accessCondition;
    }

    public static class JsonFromFileGeometry {
        public String type;
        public double[] coordinates;
    }
}
