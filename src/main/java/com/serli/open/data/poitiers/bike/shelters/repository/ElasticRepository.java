package com.serli.open.data.poitiers.bike.shelters.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.serli.open.data.poitiers.bike.shelters.rest.model.GeolocShelterResult;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Shelter;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
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

    private final JestClient client;
    public static final ElasticRepository INSTANCE = new ElasticRepository();

    private ElasticRepository() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(getElasticSearchURL())
                .multiThreaded(true)
                .build());
        client = factory.getObject();
    }

    private String getElasticSearchURL() {
        String elasticSearchURL = System.getenv("SEARCHBOX_SSL_URL");
        if (StringUtils.isEmpty(elasticSearchURL)) {
            elasticSearchURL = "http://localhost:9200";
        }
        return elasticSearchURL;
    }

    public void index(Shelter shelter) {
        Index index = new Index.Builder(shelter)
                .index(OPEN_DATA_POITIERS_INDEX)
                .type(BIKE_SHELTERS_TYPE)
                .id(String.valueOf(shelter.objectId))
                .build();
        try {
            client.execute(index);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<GeolocShelterResult> find(double lat, double lon, int size) {
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

        SearchResult searchResult = performSearchOnBikeShelters(query);

        JsonObject jsonObject = searchResult.getJsonObject();
        JsonArray jsonHits = jsonObject.get("hits").getAsJsonObject().get("hits").getAsJsonArray();

        Gson gson = new Gson();

        return StreamSupport.stream(jsonHits.spliterator(), false).map(jsonElement -> {
            JsonObject hit = jsonElement.getAsJsonObject();
            double distance = hit.get("sort").getAsDouble();
            Shelter shelter = gson.fromJson(hit.get("_source").getAsJsonObject(), Shelter.class);

            return new GeolocShelterResult(shelter, (int) (distance * 1000));
        }).collect(Collectors.toList());

    }

    public List<Shelter> all() {
        String query = "{\n" +
                "   \"query\": {\n" +
                "      \"match_all\": {}\n" +
                "   },\n" +
                "   \"size\": " + Integer.MAX_VALUE + "\n" +
                "}";
        SearchResult searchResult = performSearchOnBikeShelters(query);

        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(searchResult.getHits(Shelter.class).iterator(), Spliterator.ORDERED),
                false).map(hitResult -> hitResult.source).collect(Collectors.toList());
    }

    private SearchResult performSearchOnBikeShelters(String query) {
        Search search = new Search.Builder(query)
                .addIndex(OPEN_DATA_POITIERS_INDEX)
                .addType(BIKE_SHELTERS_TYPE)
                .build();

        SearchResult searchResult;
        try {
            searchResult = client.execute(search);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return searchResult;
    }
}
