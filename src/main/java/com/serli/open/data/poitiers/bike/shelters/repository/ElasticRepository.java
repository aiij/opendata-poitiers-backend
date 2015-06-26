package com.serli.open.data.poitiers.bike.shelters.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.serli.open.data.poitiers.bike.shelters.rest.model.GeolocResult;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Park;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Service;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Shelter;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Ticketmachine;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.Search.Builder;
import io.searchbox.core.SearchResult;

import org.apache.commons.lang3.StringUtils;

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
	public static final String ticketmachine_TYPE = "ticketmachine";
	public static final String PARK_TYPE = "park";
	public static final String SERVICE_TYPE = "service";

	private final JestClient client;
	public static final ElasticRepository INSTANCE = new ElasticRepository();
	private int size = 100;

	private ElasticRepository() {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(
				getElasticSearchURL()).multiThreaded(true).build());
		client = factory.getObject();
	}

	private String getElasticSearchURL() {
		String elasticSearchURL = System.getenv("BONSAI_URL");
		if (StringUtils.isEmpty(elasticSearchURL)) {
			elasticSearchURL = "http://localhost:9200";
		}
		return elasticSearchURL;
	}

	public void index(Shelter shelter) {
		Index index = new Index.Builder(shelter)
				.index(OPEN_DATA_POITIERS_INDEX).type(BIKE_SHELTERS_TYPE)
				.id(String.valueOf(shelter.objectId)).build();
		try {
			client.execute(index);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void index(Ticketmachine ticketmachine) {
		Index index = new Index.Builder(ticketmachine)
				.index(OPEN_DATA_POITIERS_INDEX).type(ticketmachine_TYPE)
				.id(String.valueOf(ticketmachine.objectId)).build();
		try {
			client.execute(index);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void index(Park park) {
		Index index = new Index.Builder(park).index(OPEN_DATA_POITIERS_INDEX)
				.type(PARK_TYPE).id(String.valueOf(park.objectId)).build();
		try {
			client.execute(index);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void index(Service service) {
		Index index = new Index.Builder(service)
				.index(OPEN_DATA_POITIERS_INDEX).type(SERVICE_TYPE)
				.id(String.valueOf(service.objectId)).build();
		try {
			client.execute(index);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<GeolocResult> find(double lat, double lon, int size) {
		if (size == 0) {
			size = 10;
		}

		String query = "{\n" + "  \"query\": {\n" + "    \"filtered\" : {\n"
				+ "        \"filter\" : {\n"
				+ "            \"geo_distance\" : {\n"
				+ "                \"distance\" : \"200km\",\n"
				+ "                \"location\" : {\n"
				+ "                    \"lat\" : "
				+ lat
				+ ",\n"
				+ "                    \"lon\" : "
				+ lon
				+ "\n"
				+ "                }\n"
				+ "            }\n"
				+ "        }\n"
				+ "    }\n"
				+ "  },\n"
				+ "  \"size\" : "
				+ size
				+ ",\n"
				+ "  \"sort\": [\n"
				+ "    {\n"
				+ "      \"_geo_distance\": {\n"
				+ "        \"location\": { \n"
				+ "            \"lat\" : "
				+ lat
				+ ",\n"
				+ "            \"lon\" : "
				+ lon
				+ "\n"
				+ "        },\n"
				+ "        \"order\":         \"asc\",\n"
				+ "        \"unit\":          \"km\", \n"
				+ "        \"distance_type\": \"plane\" \n"
				+ "      }\n"
				+ "    }\n" + "  ]\n" + "}";

		SearchResult searchResult = performSearch(query, null);

		JsonObject jsonObject = searchResult.getJsonObject();
		JsonArray jsonHits = jsonObject.get("hits").getAsJsonObject()
				.get("hits").getAsJsonArray();

		Gson gson = new Gson();

		return StreamSupport
				.stream(jsonHits.spliterator(), false)
				.map(jsonElement -> {
					JsonObject hit = jsonElement.getAsJsonObject();
					double distance = hit.get("sort").getAsDouble();
					Shelter shelter = gson.fromJson(hit.get("_source")
							.getAsJsonObject(), Shelter.class);

					return new GeolocResult(shelter, (int) (distance * 1000));
				}).collect(Collectors.toList());

	}

	public List<Shelter> allShelters() {
		SearchResult searchResult = performSearch(createSearchAllQuery(),
				BIKE_SHELTERS_TYPE);
		return StreamSupport
				.stream(Spliterators
						.spliteratorUnknownSize(
								searchResult.getHits(Shelter.class).iterator(),
								Spliterator.ORDERED),
						false).map(hitResult -> hitResult.source)
				.collect(Collectors.toList());
	}

	private String createSearchAllQuery() {
		return "{\n" + "   \"query\": {\n" + "      \"match_all\": {}\n"
				+ "   },\n" + "   \"size\": " + Integer.MAX_VALUE + "\n" + "}";
	}

	public List<Park> allParks() {
		SearchResult searchResult = performSearch(createSearchAllQuery(), PARK_TYPE);
		return StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(searchResult
						.getHits(Park.class).iterator(), Spliterator.ORDERED),
						false).map(hitResult -> hitResult.source)
				.collect(Collectors.toList());
	}
	
	/*public List<T> allT(class T, String type) {
		SearchResult searchResult = performSearch(createSearchAllQuery(), type);
		return StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(searchResult
						.getHits(T.class).iterator(), Spliterator.ORDERED),
						false).map(hitResult -> hitResult.source)
				.collect(Collectors.toList());
	}*/

	public List<Ticketmachine> allTicketmachines() {
		SearchResult searchResult = performSearch(createSearchAllQuery(), ticketmachine_TYPE);
		return StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(searchResult
						.getHits(Ticketmachine.class).iterator(),
						Spliterator.ORDERED), false)
				.map(hitResult -> hitResult.source)
				.collect(Collectors.toList());
	}

	public List<Service> allService() {
		SearchResult searchResult = performSearch(createSearchAllQuery(), SERVICE_TYPE);
		return StreamSupport
				.stream(Spliterators
						.spliteratorUnknownSize(
								searchResult.getHits(Service.class).iterator(),
								Spliterator.ORDERED),
						false).map(hitResult -> hitResult.source)
				.collect(Collectors.toList());
	}

	private SearchResult performSearch(String query, String type) {
		Builder builder = new Search.Builder(query)
				.addIndex(OPEN_DATA_POITIERS_INDEX);
		if (StringUtils.isNotEmpty(type)) {
			builder.addType(type);
		}
		Search search = builder.build();

		SearchResult searchResult;
		try {
			searchResult = client.execute(search);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return searchResult;
	}
}
