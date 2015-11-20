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
public class DefibrillatorTest {
    
    
    /**
     * Test of Instantation, of class Defibrillator.
     */
    @Test
    public void testInstantation() {
        System.out.println("toString");
        double[] d = new double[2];
        d[0]=1;
        d[1]=0.23;
        Defibrillator instance = new Defibrillator(     
 
                "id",
                d,
                1,
                "town",
                "place",
                2,
                "adress",
                "access");
        String expResult = "Defibrillator{" +
                ", identifier=" + instance.identifier +
                ", location=" + Arrays.toString(instance.location) +
                ", objectId=" + instance.objectId +
                ", town=" + instance.town +
                ", place=" + instance.place +
                ", addressNumber=" + instance.addressNumber +
                ", address=" + instance.address +
                ", accesCondition=" + instance.accessCondition +
                '}';
        String result = instance.toString();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getElasticType method, of class Defibrillator.
     */
    @Test
    public void testGetElasticType() {
        Defibrillator instance = new Defibrillator(     
 
                "id",
                null,
                1,
                "town",
                "place",
                2,
                "adress",
                "access"
        );
        String expResult = "defibrillator";
        String result = instance.getElasticType();
        assertEquals(expResult, result);
        
        
    }
    
}
