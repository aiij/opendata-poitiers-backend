package com.serli.open.data.poitiers.elasticsearch;

import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.DeleteMapping;
import io.searchbox.indices.mapping.PutMapping;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    public static void createMapping(String indexName, String type, String esURL) {
        try (RuntimeJestClient client = createClient()) {
            DeleteMapping deleteMapping = new DeleteMapping.Builder(indexName, type).build();
            client.execute(deleteMapping);

            Settings settings = SettingsRepository.INSTANCE.getAllSettings();
            Object mappingFile;
            
            /*try {
                mappingFile = IOUtils.toString(ElasticUtils.class.getResourceAsStream(mappingFilePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            
            mappingFile = settings.mapping.get(type);
            System.out.println(mappingFile);
            PutMapping putMapping = new PutMapping.Builder(indexName, type, mappingFile).build();
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
