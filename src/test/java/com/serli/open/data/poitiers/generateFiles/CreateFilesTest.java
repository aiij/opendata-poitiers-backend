/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.generateFiles;

import com.serli.open.data.poitiers.api.AdminEndPoint;
import com.serli.open.data.poitiers.jobs.configuration.GenerateConfigurationFiles;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author dupar_000
 */
public class CreateFilesTest {
    
    @Test
    public void generateConfFileTest() throws IOException, JSONException{
        String myString = "{\"properties\":{\"0\":{\"champJson\":\"champJson1\",\"champES\":\"champES1\",\"mapping\":true},\"1\":{\"champJson\":\"champJson2\",\"champES\":\"champES2\",\"mapping\":true},\"2\":{\"champES\":\"location\"}},\"url\":\"http\",\"type\":\"testTemp\"}";
        String chaineResult = "champES1 = champJson1\nchampES2 = champJson2\nlocation = location\n";
        String file = System.getProperty("user.dir")+"/src/main/resources/conf/testTemp.properties";        
        GenerateConfigurationFiles.generateConfFile(myString);
        String chaine = this.readFile(file);
        assertEquals(chaineResult,chaine);
    }
    
    public String readFile(String file) throws FileNotFoundException, IOException{
        String chaine="";
        InputStream ips=new FileInputStream(file);
        InputStreamReader ipsr=new InputStreamReader(ips);
        try (BufferedReader br = new BufferedReader(ipsr)) {
            String ligne;
            while ((ligne=br.readLine())!=null){
                chaine+=ligne+"\n";
            }
        }
        new File(file).delete();
        return chaine;
    }
    
    @Test
    public void generateESMappingTest() throws IOException, JSONException{
        String myString = "{\"properties\":{\"0\":{\"champJson\":\"champJson1\",\"champES\":\"champES1\",\"mapping\":true},\"1\":{\"champJson\":\"champJson2\",\"champES\":\"champES2\",\"mapping\":true},\"2\":{\"champES\":\"location\"}},\"url\":\"http\",\"type\":\"testTemp\"}";
        String chaineResult = "{\n" +
                                "   \"testTemp\" : {\n" +
                                "       \"properties\" : {\n" +
                                "          \"champES1\" : { \n" +
                                "               \"type\" : \"string\", \n" +
                                "               \"index\" : \"not_analyzed\" \n" +
                                "           }, \n" +
                                "          \"champES2\" : { \n" +
                                "               \"type\" : \"string\", \n" +
                                "               \"index\" : \"not_analyzed\" \n" +
                                "           }, \n" +
                                "          \"location\" : { \n" +
                                "               \"type\" : \"geo_point\"\n" +
                                "           } \n" +
                                "       } \n" +
                                "   } \n" +
                                "} \n";
        
        String file = System.getProperty("user.dir") + "/src/main/resources/elasticsearch/mappings/testTemp.json";
        GenerateConfigurationFiles.generateESMapping(myString);
        String chaine = this.readFile(file);
        assertEquals(chaineResult,chaine);
    }
}