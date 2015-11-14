package com.serli.open.data.poitiers.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.util.Properties;

import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.createIndexIfNotExists;
import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.createMapping;
import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.getElasticSearchURL;
import static com.serli.open.data.poitiers.repository.ElasticRepository.*;

/**
 * Created by chris on 13/11/15.
 */
public abstract class ImportDataJob<T> {
    public void createIndexAndLoad(){
        createIndexIfNotExists(OPEN_DATA_POITIERS_INDEX, getElasticSearchURL());

        createMapping(OPEN_DATA_POITIERS_INDEX, BIKE_SHELTERS_TYPE, mappingFilePath(), getElasticSearchURL());

        Properties properties = new Properties();
        try {
            properties.load(ImportDataJob.class.getResourceAsStream("/application.properties"));
            InputStream requestInputStream = Request.Get(properties.getProperty(fileURLPropertyName())).execute().returnContent().asStream();
            File tempFile = File.createTempFile("open-data-poitiers", "txt");
            try(FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile)){
                IOUtils.copy(requestInputStream, tempFileOutputStream);
            }
            tempFile.deleteOnExit();

            InputStream inputData = Files.newInputStream(tempFile.toPath());
            ObjectMapper objectMapper = new ObjectMapper();

            T elementFromFile = objectMapper.readValue(inputData, getParametrizedType());
            indexRootElement(elementFromFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<T> getParametrizedType(){
        return (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }

    protected abstract void indexRootElement(T rootElement);

    protected abstract String fileURLPropertyName();

    protected abstract String mappingFilePath();
}
