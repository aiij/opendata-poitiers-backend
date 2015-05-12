package com.serli.open.data.poitiers.bike.shelters.rest;

import com.serli.open.data.poitiers.bike.shelters.repository.ElasticRepository;
import com.serli.open.data.poitiers.bike.shelters.repository.InMemoryRepository;
import com.serli.open.data.poitiers.bike.shelters.rest.model.GeolocShelterResult;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Shelter;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Gets;
import net.codestory.http.annotations.Prefix;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chris on 04/05/15.
 */
@Prefix("bike-shelters")
public class ShelterEndPoint {


    @Get("/")
    public String home(){
        return "<p>GET <a href=\"all\">all</a> : all helters</p>" +
               "<p>GET <a href=\"find?lat=46.578636&lon=0.337959\">find?lat=:lat&lon=:lon&size=:size</a> : search closest shelters</p>";
    }

    @Get("/all")
    public List<Shelter> all(){
        return ElasticRepository.INSTANCE.all();
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocShelterResult> find(double lat, double lon, int size){
        return ElasticRepository.INSTANCE.find(lat, lon, size);
    }

}
