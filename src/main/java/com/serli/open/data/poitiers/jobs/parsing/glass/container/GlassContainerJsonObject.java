/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.parsing.glass.container;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author dupar_000
 */
public class GlassContainerJsonObject {
    public String type;
    public JsonFromFileProperties properties;
    public JsonFromFileGeometry geometry;
    
    public static class JsonFromFileProperties {
        @JsonProperty("gml_id")
        public String gmlId;
        @JsonProperty("OBJECTID")
        public int objectId;
        @JsonProperty("Num\u00e9ro_de_borne")
        public int numBorne;
        @JsonProperty("Volume")
        public int volume;
        @JsonProperty("Fr\u00e9quence_de_la_collecte")
        public String freqCollect;
        @JsonProperty("Adresse")
        public String adresse;
        @JsonProperty("Observation")
        public String observation;
        @JsonProperty("Jour_de_la_collecte")
        public String jourCollect;
        

    }
    
    public static class JsonFromFileGeometry {
        public String type;
        public double[] coordinates;
    }
}
