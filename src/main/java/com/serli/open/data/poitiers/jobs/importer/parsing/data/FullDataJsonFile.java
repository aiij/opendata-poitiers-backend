/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer.parsing.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 *
 * @author Julien L
 */
public class FullDataJsonFile {
    public String type;
    public Map<String,Object> crs;
    @JsonProperty("features")
    public DataJsonObject[] data;
}
