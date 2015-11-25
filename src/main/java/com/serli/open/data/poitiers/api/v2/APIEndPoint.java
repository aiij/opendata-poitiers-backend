package com.serli.open.data.poitiers.api.v2;

import com.serli.open.data.poitiers.api.v1.model.GeolocResultV1;
import com.serli.open.data.poitiers.api.v2.model.GeolocResult;
import com.serli.open.data.poitiers.repository.OpenDataRepository;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

import java.util.List;

/**
 * Created by chriswoodrow on 25/11/2015.
 */
@Prefix("api/v2")
public class APIEndPoint {
    @Get(":type/all")
    public List<?> getAll(String type) {
        return OpenDataRepository.INSTANCE.getAll(type);
    }

    @Get(":type/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocResult<Object>> find(String type, double lat, double lon, int size) {
        return OpenDataRepository.INSTANCE.find(lat, lon, size, type);
    }
}
