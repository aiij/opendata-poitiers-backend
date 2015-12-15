package com.serli.open.data.poitiers.api.v1;

import com.serli.open.data.poitiers.api.v1.model.GeolocResultV1;
import com.serli.open.data.poitiers.api.v2.model.Shelter;
import com.serli.open.data.poitiers.repository.OpenDataRepository;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chris on 04/05/15.
 */
@Prefix("bike-shelters")
public class ShelterEndPoint {
    @Get("/all")
    public final List<Shelter> getAll(){
        return OpenDataRepository.INSTANCE.getAll(Shelter.class);
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocResultV1<Shelter>> find(double lat, double lon, int size){
        return OpenDataRepository.INSTANCE.find(lat, lon, size, Shelter.class)
                .stream()
                .map(georesult -> new GeolocResultV1<>(georesult.pointOfInterest, georesult.distanceInMeters))
                .collect(Collectors.toList());
    }
}
