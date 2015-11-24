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
public class DisabledParkingTest {
      /**
     * Test of Instantation, of class DisabledParking.
     */
    @Test
    public void testInstantation() {
        
        double[] d = new double[2];
        d[0]=1;
        d[1]=0.23;
        DisabledParking instance = new DisabledParking(1,"id", "district", "state", "comment", "town", d);
       
        String expResult = "DisabledParking{" +
                "objectId=" + instance.objectId +
                ", identifier='" + instance.identifier + '\'' +
                ", district='" + instance.district + '\'' +
                ", state='" + instance.state + '\'' +
                ", comment='" + instance.comment + '\'' +
                ", town='" + instance.town + '\'' +
                ", location=" + Arrays.toString(instance.location) +
                '}';
        String result = instance.toString();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getElasticType method, of class DisabledParking.
     */
    @Test
    public void testGetElasticType() {
        DisabledParking instance = new DisabledParking(1,"id", "district", "state", "comment", "town",null);
        String expResult = "disabled-parking";
        String result = instance.getElasticType();
        assertEquals(expResult, result);
        
        
    }
}
