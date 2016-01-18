/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer.parsing.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Julien L
 */
public class DataJsonObject {
    public String type;
    //Stores json files properties
    public Map<String, Object> properties = new HashMap<> ();
    public JsonFromFileGeometry geometry;
    
    /*public static class JsonFromFileProperties {


    }*/
    
    public static class JsonFromFileGeometry {
        public String type;
        public double[] coordinates;
    }
}
