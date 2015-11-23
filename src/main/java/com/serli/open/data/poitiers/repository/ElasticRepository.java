package com.serli.open.data.poitiers.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.serli.open.data.poitiers.api.model.ElasticTypedObject;
import com.serli.open.data.poitiers.api.model.GeolocResult;
import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.elasticsearch.RuntimeJestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by chris on 04/05/15.
 */
public class ElasticRepository {

    public static final String OPEN_DATA_POITIERS_INDEX = "open-data-poitiers";
    public static final String BIKE_SHELTERS_TYPE = "bike-shelters";
    public static final String DISABLED_PARKING_TYPE = "disabled-parking";
    public static final String GLASS_CONTAINER_TYPE = "glass-container";
    public static final String DEFIBRILLATOR_TYPE = "defibrillator";


    private final RuntimeJestClient client;
    public static final ElasticRepository INSTANCE = new ElasticRepository();

    private ElasticRepository() {
        client = ElasticUtils.createClient(ElasticUtils.getElasticSearchURL());
    }

    private void index(Object object, String type) {
        Index index = new Index.Builder(object)
                .index(OPEN_DATA_POITIERS_INDEX)
                .type(type)
                .build();
        client.execute(index);
    }

    public void index(ElasticTypedObject elasticTypedObject) {
        index(elasticTypedObject, elasticTypedObject.getElasticType());
    }

    public <T extends ElasticTypedObject> List<GeolocResult<T>> find(double lat, double lon, int size, Class<T> clazz) {
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

        String elasticType = getElasticType(clazz);
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

    public <T extends ElasticTypedObject> List<T> all(Class<T> clazz) {
        String elasticType = getElasticType(clazz);

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

    // FIXME : un peu vilain mais bon...
    private <T extends ElasticTypedObject> String getElasticType(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance().getElasticType();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private SearchResult performSearchOnType(String query, String type) {
        Search search = new Search.Builder(query)
                .addIndex(OPEN_DATA_POITIERS_INDEX)
                .addType(type)
                .build();

        return client.execute(search);
    }

}
