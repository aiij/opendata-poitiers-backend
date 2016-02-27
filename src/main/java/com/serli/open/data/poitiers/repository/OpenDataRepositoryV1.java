package com.serli.open.data.poitiers.repository;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.serli.open.data.poitiers.api.v2.model.GeolocResult;
import com.serli.open.data.poitiers.api.v2.model.settings.DataSource;
import com.serli.open.data.poitiers.api.v2.model.settings.Settings;
import com.serli.open.data.poitiers.repository.ElasticSearchRepository;
import com.serli.open.data.poitiers.repository.OpenDataRepository;
import com.serli.open.data.poitiers.repository.SettingsRepository;
import static com.serli.open.data.poitiers.repository.ElasticSearchRepository.OPEN_DATA_POITIERS_INDEX;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *  @author Julien L
 */
public class OpenDataRepositoryV1 extends ElasticSearchRepository {

    public static final String BIKE_SHELTERS_TYPE = "bike-shelters";
    public static final String DISABLED_PARKINGS_TYPE = "disabled-parkings";
    public static final String GLASS_CONTAINERS_TYPE = "glass-containers";
    public static final String DEFIBRILLATORS_TYPE = "defibrillators";

    public static final OpenDataRepositoryV1 INSTANCE = new OpenDataRepositoryV1();

    public final BiMap<Class<?>, String> classToTypeCache = HashBiMap.create();

    private void index(Object object, String type) {
        Index index = new Index.Builder(object)
                .index(OPEN_DATA_POITIERS_INDEX)
                .type(type)
                .build();
        client.execute(index);
    }

    public void index(Object object) {
        String type = getElasticType(object.getClass());
        index(object, type);
    }

    public <T> List<GeolocResult<T>> find(double lat, double lon, int size, Class<T> clazz) {
        String elasticType = getElasticType(clazz);

        return find(lat, lon, size, clazz, elasticType);

    }

    public List<GeolocResult<?>> find(double lat, double lon, int size, String elasticType){
        Class clazz = getClassFromType(elasticType);
        return find(lat, lon, size, clazz, elasticType);
    }

    private <T> List<GeolocResult<T>> find(double lat, double lon, int size, Class<T> clazz, String elasticType) {
        if (size == 0) {
            size = 10;
        }

        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"filtered\" : {\n" +
                "        \"filter\" : {\n" +
                "            \"geo_distance\" : {\n" +
                "                \"distance\" : \"200km\",\n" +
                "                \"location\" : {\n" +
                "                    \"lat\" : " + lat + ",\n" +
                "                    \"lon\" : " + lon + "\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "  },\n" +
                "  \"size\" : " + size + ",\n" +
                "  \"sort\": [\n" +
                "    {\n" +
                "      \"_geo_distance\": {\n" +
                "        \"location\": { \n" +
                "            \"lat\" : " + lat + ",\n" +
                "            \"lon\" : " + lon + "\n" +
                "        },\n" +
                "        \"order\":         \"asc\",\n" +
                "        \"unit\":          \"km\", \n" +
                "        \"distance_type\": \"plane\" \n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        SearchResult searchResult = performSearchOnType(query, elasticType);

        JsonObject jsonObject = searchResult.getJsonObject();
        JsonArray jsonHits = jsonObject.get("hits").getAsJsonObject().get("hits").getAsJsonArray();

        Gson gson = new Gson();

        return StreamSupport.stream(jsonHits.spliterator(), false).map(jsonElement -> {
            JsonObject hit = jsonElement.getAsJsonObject();
            double distance = hit.get("sort").getAsDouble();
            T result = gson.fromJson(hit.get("_source").getAsJsonObject(), clazz);

            return new GeolocResult<T>(result, (int) (distance * 1000));
        }).collect(Collectors.toList());
    }

    public <T> List<T> getAll(Class<T> clazz) {
        String elasticType = getElasticType(clazz);
        return getAll(clazz, elasticType);
    }

    public List<?> getAll(String type) {
        Class<?> clazz = getClassFromType(type);
        return getAll(clazz, type);
    }

    private <T> List<T> getAll(Class<T> clazz, String elasticType) {
        String query = "{\n" +
                "   \"query\": {\n" +
                "      \"match_all\": {}\n" +
                "   },\n" +
                "   \"size\": " + Integer.MAX_VALUE + "\n" +
                "}";
        SearchResult searchResult = performSearchOnType(query, elasticType);

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(searchResult.getHits(clazz).iterator(), Spliterator.ORDERED),
                false).map(hitResult -> hitResult.source).collect(Collectors.toList());
    }


    private void reloadClassTypeCache(){
        classToTypeCache.clear();
        Settings settings = SettingsRepository.INSTANCE.getAllSettings();
        for (Map.Entry<String, DataSource> entry : settings.sources.entrySet()) {
            try {
                Class<?> clazz = Class.forName(entry.getValue().mappingClass);
                classToTypeCache.put(clazz, entry.getKey());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getElasticType(Class<?> clazz) {
        if(classToTypeCache.isEmpty()){
            reloadClassTypeCache();
        }

        String type = classToTypeCache.get(clazz);
        if(type == null){
            reloadClassTypeCache();
            type = classToTypeCache.get(clazz);
        }

        if(type == null){
            throw  new RuntimeException("Type not found for class : " + clazz.getName());
        }
        return type;
    }

    public Class<?> getClassFromType(String type){
        if(classToTypeCache.isEmpty()){
            reloadClassTypeCache();
        }

        Class<?> clazz = classToTypeCache.inverse().get(type);
        if(type == null){
            reloadClassTypeCache();
            clazz = classToTypeCache.inverse().get(type);
        }
        return clazz;
    }

    private SearchResult performSearchOnType(String query, String type) {
        Search search = new Search.Builder(query)
                .addIndex(OPEN_DATA_POITIERS_INDEX)
                .addType(type)
                .build();

        return client.execute(search);
    }

}
