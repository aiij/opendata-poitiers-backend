package com.serli.open.data.poitiers.elasticsearch;


import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
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
import com.serli.open.data.poitiers.jobs.importer.ImportAllDataJob;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import java.util.Map.Entry;
//import com.serli.open.data.poitiers.jobs.importer.ImportAnyDataJob;

/**
 * Embedded ES node for dev purpose, <b>Do not use in production, data will be lost</b>. Data is stored in temp directory : <b>tmp/es-local-data</b>.
 */
public class DeveloppementESNode {

    public static final String ES_LOCAL_DATA = "tmp/es-local-data";
    private static final Logger LOGGER = LoggerFactory.getLogger(DeveloppementESNode.class);

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
        // importing data
        ImportAllDataJob.elasticType = "defibrillators";
        ImportAllDataJob.filename = "conf/defibrillator.properties";
        run(ImportAllDataJob.class);
        /*for (Entry<String, DataSource> entry : SettingsRepository.INSTANCE.getAllSettings().sources.entrySet())
        {
            ImportAllDataJob.elasticType = entry.getKey();
            ImportAllDataJob.filename = entry.getValue().configFile;
            run(ImportAllDataJob.class);
        }*/
    }
}
