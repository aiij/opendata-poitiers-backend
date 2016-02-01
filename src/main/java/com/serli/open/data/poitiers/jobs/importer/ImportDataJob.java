package com.serli.open.data.poitiers.jobs.importer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.jobs.Job;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import com.serli.open.data.poitiers.utils.ReflexiveUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.fluent.Request;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.createIndexIfNotExists;
import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.createMapping;
import static com.serli.open.data.poitiers.elasticsearch.ElasticUtils.getElasticSearchURL;
import static com.serli.open.data.poitiers.repository.OpenDataRepository.*;

/**
 * Created by chris on 13/11/15.
 */
public abstract class ImportDataJob<T> implements Job {
    static {
        // Trust getAll certificate... FIXME
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {}

                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {}
                }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLContext.setDefault(sslContext);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
       createIndexAndLoad();
    }

   public void createIndexAndLoad(){
 
        createIndexIfNotExists(OPEN_DATA_POITIERS_INDEX, getElasticSearchURL());

        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        DataSource dataSource = settings.sources.get(getElasticType());
        if(dataSource == null){
            throw new RuntimeException("DataSource is not in settings : " + getElasticType());
        }
        
        createMapping(OPEN_DATA_POITIERS_INDEX, getElasticType(), dataSource.mappingFilePath, getElasticSearchURL());
       try {
            InputStream requestInputStream;
           
            if(dataSource.openDataFileURL.equals("/test-ES.json")){
              
                requestInputStream = ImportDataJob.class.getResourceAsStream(dataSource.openDataFileURL);
            }else
                requestInputStream = Request.Get(dataSource.openDataFileURL).execute().returnContent().asStream();
            File tempFile = File.createTempFile("open-data-poitiers", "txt");
            try(FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile)){
                IOUtils.copy(requestInputStream, tempFileOutputStream);
            }
            tempFile.deleteOnExit();System.out.println(tempFile.toPath());
            InputStream inputData = Files.newInputStream(tempFile.toPath());
            ObjectMapper objectMapper = new ObjectMapper();
          
            try {
                 T elementFromFile = objectMapper.readValue(inputData, getParametrizedType());
                  indexRootElement(elementFromFile);
            } catch(Exception j) {
                System.out.println("exep "+ j.toString());
            }
           
            
           
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
  

    private Class<T> getParametrizedType(){
        return (Class<T>) ReflexiveUtils.getParametrizedType(getClass());
    }

    protected abstract void indexRootElement(T rootElement);

//    protected abstract String fileURLPropertyName();
//
//    protected abstract String mappingFilePath();

    protected abstract String getElasticType();
}
