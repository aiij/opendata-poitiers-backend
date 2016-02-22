/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.elasticsearch;

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
public class DeveloppementESNodeTest {
    
    
    @After
    public void tearDown() {
            DeveloppementESNode.getNode().stop();
    }

    /**
     * Test of createDevNode method, of class DeveloppementESNode.
     */
    @Test
    public void testCreateDevNode() {
        assertEquals(null, DeveloppementESNode.getNode());
        DeveloppementESNode.createDevNode();
        //System.out.println(DeveloppementESNode.getNode()==null);
        assertNotEquals(true, DeveloppementESNode.getNode().isClosed());
    }
    
}
