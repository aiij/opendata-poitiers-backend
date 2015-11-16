package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.model.DisabledParking;
import com.serli.open.data.poitiers.api.model.GeolocResult;
import com.serli.open.data.poitiers.api.model.Shelter;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

import java.util.List;

/**
 * Created by chris on 16/11/15.
 */
@Prefix("disabled-parkings")
public class DisableParkingEndPoint {
    @Get("/all")
    public List<DisabledParking> all(){
        return ElasticRepository.INSTANCE.all(DisabledParking.class);
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocResult<DisabledParking>> find(double lat, double lon, int size){
        return ElasticRepository.INSTANCE.find(lat, lon, size, DisabledParking.class);
    }
}
