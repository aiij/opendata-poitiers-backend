/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.elasticsearch;

import com.serli.open.data.poitiers.jobs.importer.v2.ImportDataJob;
import io.searchbox.client.JestResult;
import io.searchbox.indices.IndicesExists;
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
public class ElasticUtilsTest {

    static RuntimeJestClient client;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
        client.close();

    }

    /**
     * Test of createClient method, of class ElasticUtils.
     */
    @Test
    public void testCreateClient() {
        assertEquals(client, null);
        client = ElasticUtils.createClient();
        assertNotEquals(client, null);

    }

    @Test
    public void testCreateIndexIfNotExists() {
        System.out.println("createIndexIfNotExists");
        DeveloppementESNode.createDevNode();
        RuntimeJestClient client = ElasticUtils.createClient();
        ElasticUtils.createIndexIfNotExists("index1", ElasticUtils.getElasticSearchURL());
        IndicesExists indicesExists = new IndicesExists.Builder("index1").build();
        JestResult indexExistsResult = client.execute(indicesExists);
        boolean found = indexExistsResult.getJsonObject().getAsJsonPrimitive("found").getAsBoolean();
        if (found) {
            System.out.println("yess ");
        }
        assertTrue(found);

    }

   
}
