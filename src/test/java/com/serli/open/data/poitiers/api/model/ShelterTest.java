/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.api.model;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ibrahim
 */
public class ShelterTest {
    
       /**
     * Test of Instantation, of class Shelter.
     */
    @Test
    public void testInstantation() {
        
        double[] d = new double[2];
        d[0]=1;
        d[1]=0.23;
        Shelter instance = new Shelter("type", 5, d, 5, "adress");
       
        String expResult = "Shelter{" +
                "type='" + instance.type + '\'' +
                ", capacity=" + instance.capacity +
                ", location=" + Arrays.toString(instance.location) +
                ", objectId=" + instance.objectId +
                '}';
        String result = instance.toString();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getElasticType method, of class Shelter.
     */
    @Test
    public void testGetElasticType() {
        Shelter instance = new Shelter("type", 5, null, 5, "adress");
        String expResult = "bike-shelters";
        String result = instance.getElasticType();
        assertEquals(expResult, result);
        
        
    }
    
}
