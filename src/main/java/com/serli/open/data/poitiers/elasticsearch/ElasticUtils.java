package com.serli.open.data.poitiers.elasticsearch;

import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.DeleteMapping;
import io.searchbox.indices.mapping.PutMapping;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import org.json.JSONObject;

public abstract class ElasticUtils {
    private ElasticUtils() {}
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticUtils.class);

    public static RuntimeJestClient createClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(getElasticSearchURL())
                .multiThreaded(true)
                .build());
        return new RuntimeJestClientAdapter(factory.getObject());
    }

    public static void createIndexIfNotExists(String indexName, String esURL) {
        try (RuntimeJestClient client = createClient()) {
            IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
            JestResult indexExistsResult = client.execute(indicesExists);
            boolean found = indexExistsResult.getJsonObject().getAsJsonPrimitive("found").getAsBoolean();

            if (!found) {
                LOGGER.info("Creating index : " + indexName);
                CreateIndex createIndex =
                        new CreateIndex.Builder(indexName)
                                .settings(ImmutableSettings.settingsBuilder().build().getAsMap())
                                .build();
                JestResult jestResult = client.execute(createIndex);
                if (!jestResult.isSucceeded()) {
                    throw new IllegalStateException("Index creation failed : " + jestResult.getJsonString());
                }
            }
        }
    }

    public static void createMapping(String indexName, String type, String mappingFilePath, String esURL) {
        try (RuntimeJestClient client = createClient()) {
            DeleteMapping deleteMapping = new DeleteMapping.Builder(indexName, type).build();
            client.execute(deleteMapping);
            
            File f = new File(System.getProperty("user.dir")+ "/src/main/resources" + mappingFilePath);
            String content = "";            
            InputStream ips;
            InputStreamReader ipsr;
            
            try {
                ips = new FileInputStream(f.getAbsolutePath());
                ipsr = new InputStreamReader(ips, "UTF-8");
                BufferedReader br = new BufferedReader(ipsr);
                String ligne;
                while ((ligne = br.readLine())!=null) {
                   content += ligne + "\n";
                }
                br.close();
            } catch (UnsupportedEncodingException ex) {
                java.util.logging.Logger.getLogger(ElasticUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ElasticUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            PutMapping putMapping = new PutMapping.Builder(indexName, type, content).build();
            client.execute(putMapping);
        }
    }

    public static String getElasticSearchURL() {
        String elasticSearchURL = System.getenv("SEARCHBOX_SSL_URL");
        if (StringUtils.isEmpty(elasticSearchURL)) {
            elasticSearchURL = "http://localhost:9200";
        }
        return elasticSearchURL;
    }
}
