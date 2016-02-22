package com.serli.open.data.poitiers.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.serli.open.data.poitiers.api.v2.model.GeolocResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by chris on 04/05/15.
 */
public class OpenDataRepository extends ElasticSearchRepository {

    /*public static final String BIKE_SHELTERS_TYPE = "bike-shelters";
    public static final String DISABLED_PARKINGS_TYPE = "disabled-parkings";
    public static final String GLASS_CONTAINERS_TYPE = "glass-containers";
    public static final String DEFIBRILLATORS_TYPE = "defibrillators";*/

    public static final OpenDataRepository INSTANCE = new OpenDataRepository();
    
    public static String ELASTIC_TYPE = "";
    public static Class MAPPING_CLASS = Map.class;
    
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
        String elasticType = ELASTIC_TYPE;
        return find(lat, lon, size, clazz, elasticType);

    }

    public List<GeolocResult<?>> find(double lat, double lon, int size, String elasticType){
        Class clazz = MAPPING_CLASS;
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
        String elasticType = ELASTIC_TYPE;
        return getAll(clazz, elasticType);
    }

    public List<?> getAll(String type) {
        Class<?> clazz = MAPPING_CLASS;
        return getAll(clazz, type);
    }


    private <T> List<T> getAll(Class<T> clazz, String elasticType) {         
        String query = "{\n" +
                "   \"query\": {\n" +
                "       \"match_all\": {}\n" +
                "   },\n" +
                "   \"size\": " + Integer.MAX_VALUE + "\n" +
                "}";
        
        SearchResult searchResult = performSearchOnType(query, elasticType);
        
        JsonObject jsonObject = searchResult.getJsonObject();
        JsonArray jsonHits = jsonObject.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
        
        Gson gson = new Gson();

        return StreamSupport.stream(jsonHits.spliterator(), false).map(jsonElement -> {
                T result = gson.fromJson(jsonElement.getAsJsonObject().get("_source").getAsJsonObject(), clazz);
                return result;
        }).collect(Collectors.toList());
    }


    public String getElasticType(Class<?> clazz) {
        return ELASTIC_TYPE;
    }

    public Class<?> getClassFromType(String type){
        return MAPPING_CLASS;
    }

    private SearchResult performSearchOnType(String query, String type) {
        Search search = new Search.Builder(query)
                .addIndex(OPEN_DATA_POITIERS_INDEX)
                .addType(type)
                .build();

        return client.execute(search);
    }
}