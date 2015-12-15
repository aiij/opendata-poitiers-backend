package com.serli.open.data.poitiers.geolocation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by chris on 13/11/15.
 */
public class GeolocationAPIClient {
    public static LatLon geocode(String query){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Content content = Request.Get("http://api-adresse.data.gouv.fr/search/?q=" + URLEncoder.encode(query, "UTF-8")).execute().returnContent();
            Map<String,Object> map = objectMapper.readValue(content.asStream(), Map.class);
            

            Optional<LatLon> firstResultCoordinates = map.entrySet().stream()
                    .filter(entry -> "features".equals(entry.getKey()))
                    .map(entry -> entry.getValue())
                    .flatMap(feature -> ((List<Map<String, Object>>) feature).stream())
                    .flatMap(feature -> feature.entrySet().stream())
                    .filter(entry -> "geometry".equals(entry.getKey()))
                    .flatMap(entry -> ((Map<String, List<Double>>) entry.getValue()).entrySet().stream())
                    .filter(entry -> "coordinates".equals(entry.getKey()))
                    .map(entry -> entry.getValue())
                    .map(doubles -> new LatLon(doubles.get(1), doubles.get(0)))
                    .findFirst();

            return firstResultCoordinates.orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Address reverseGeoCode(LatLon latLon){
        ObjectMapper objectMapper = new ObjectMapper();

//        if (1 == 1){
//            return new Address("","","");
//        }

        try {
            Response response = Request.Get("http://api-adresse.data.gouv.fr/reverse/?lon=" + latLon.lon + "&lat=" + latLon.lat).execute();
            HttpResponse httpResponse = response.returnResponse();
            if(httpResponse.getStatusLine().getStatusCode() != 200){
                return null;
            }

            Map<String,Object> map = objectMapper.readValue(httpResponse.getEntity().getContent(), Map.class);

            return map.entrySet().stream()
                    .filter(entry -> "features".equals(entry.getKey()))
                    .map(entry -> entry.getValue())
                    .flatMap(feature -> ((List<Map<String, Object>>) feature).stream())
                    .flatMap(feature -> feature.entrySet().stream())
                    .filter(entry -> "properties".equals(entry.getKey()))
                    .map(entry -> {
                        Map<String, String> properties = (Map<String, String>) entry.getValue();
                        return new Address(properties.get("name"),properties.get("postcode"),properties.get("city"));
                    })
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
