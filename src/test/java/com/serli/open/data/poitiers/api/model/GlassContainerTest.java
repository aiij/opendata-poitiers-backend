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
public class GlassContainerTest {
    
         /**
     * Test of Instantation, of class GlassContainer.
     */
    @Test
    public void testInstantation() {
        
        double[] d = new double[2];
        d[0]=1;
        d[1]=0.23;
        GlassContainer instance = new GlassContainer(1, 5, "freq", "observation", "jour", d, 4, "adress");
               
       
        String expResult = "Container{" +
                ", num_born=" + instance.numeroBorne +
                ", volume=" + instance.volume +
                 ", freq_collect=" + instance.freq_collect +
                 ", observation=" + instance.observation +
                 ", jour_collect=" + instance.jour_collect +
                ", location=" + Arrays.toString(instance.location) +
                ", objectId=" + instance.objectId +
                '}';
        String result = instance.toString();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getElasticType method, of class DisabledParking.
     */
    @Test
    public void testGetElasticType() {
        GlassContainer instance = new GlassContainer(1, 5, "freq", "observation", "jour", null, 4, "adress");
        String expResult = "glass-container";
        String result = instance.getElasticType();
        assertEquals(expResult, result);
        
        
    }
    
}
