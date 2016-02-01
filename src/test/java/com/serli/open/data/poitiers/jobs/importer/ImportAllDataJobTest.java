/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.jobs.importer;


import org.junit.BeforeClass;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.serli.open.data.poitiers.elasticsearch.DeveloppementESNode;
import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.elasticsearch.RuntimeJestClient;
import com.serli.open.data.poitiers.jobs.settings.ReloadDefaultSettings;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.serli.open.data.poitiers.jobs.JobRunner.run;
import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import io.searchbox.core.Search;

import io.searchbox.core.SearchResult;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;



/**
 *
 * @author ibrahim DELLAL
 */
public class ImportAllDataJobTest {
    public static final String ES_LOCAL_DATA = "tmp/es-local-data";
    private static final Logger LOGGER = LoggerFactory.getLogger(DeveloppementESNode.class);
    
    protected final RuntimeJestClient client = ElasticUtils.createClient();
    
    
/**
 *
 * create ES node with importing test-ES.json file wich contain three instance
 */
    @BeforeClass
    public static void beforecl(){
        createDevNode();
        // sleep(10) to be sure that all asynchronous process launched by createDevNode() are finished 
        sleep(10);
    }
    
/**
 *
 * test if createDevNode() has added the test-ES.json file to ES. 
 * test if the number of instances equals to three
 */ 
    @Test
    public void testImporDataToElastic() {
        Assert.assertEquals(3,getNbrInstance());
    }

    public static void createDevNode() {
        LOGGER.info("Creating dev ES node ...");
        Path localDevDataDirectory = Paths.get(ES_LOCAL_DATA);
        try {
            FileUtils.deleteDirectory(localDevDataDirectory.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Settings settings = ImmutableSettings.builder()
                .put("http.port", "9200")
                .put("network.host", "localhost")
                .put("path.data", ES_LOCAL_DATA)
                .build();

        Node node = NodeBuilder.nodeBuilder()
                .local(true)
                .data(true)
                .clusterName("elasticSearch" + UUID.randomUUID())
                .settings(settings)
                .build();
        node.start();
        // loading settings
        run(ReloadDefaultSettings.class);
        ImportAllDataJob.elasticType = "test-ES";
        ImportAllDataJob.filename = "conf/test-ES.properties";
        run(ImportAllDataJob.class);

        
    }
    public int getNbrInstance(){
         String query = "{\n" +
                    "   \"query\": {\n" +
                    "       \"match_all\": {}\n" +
                    "   },\n" +
                    "   \"size\": " + Integer.MAX_VALUE + "\n" +
                    "}";
        String elasticType = "test-ES";
        SearchResult searchResult = performSearchOnType(query, elasticType);
        System.out.println(elasticType);
        JsonObject jsonObject = searchResult.getJsonObject();
        JsonArray jsonHits = jsonObject.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
        System.out.println();
        return jsonHits.size();
       
    }
     private SearchResult performSearchOnType(String query, String type) {
        Search search = new Search.Builder(query)
                .addIndex(OPEN_DATA_POITIERS_INDEX)
                .addType(type)
                .build();

        return client.execute(search);
    }
    
    private static void sleep(int seconds) {

        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
   
   
}
