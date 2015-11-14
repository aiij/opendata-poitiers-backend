package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.repository.ElasticRepository;
import com.serli.open.data.poitiers.api.model.GeolocShelterResult;
import com.serli.open.data.poitiers.api.model.Shelter;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

import java.util.List;

/**
 * Created by chris on 04/05/15.
 */
@Prefix("bike-shelters")
public class ShelterEndPoint {
    @Get("/all")
    public List<Shelter> all(){
        return ElasticRepository.INSTANCE.all();
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocShelterResult> find(double lat, double lon, int size){
        return ElasticRepository.INSTANCE.find(lat, lon, size);
    }

}
