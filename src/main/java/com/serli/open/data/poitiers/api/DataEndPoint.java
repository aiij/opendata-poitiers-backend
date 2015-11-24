package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.model.DisabledParking;
import com.serli.open.data.poitiers.api.model.ElasticTypedObject;
import com.serli.open.data.poitiers.api.model.GeolocResult;
import com.serli.open.data.poitiers.api.model.Shelter;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import com.serli.open.data.poitiers.utils.ReflexiveUtils;
import net.codestory.http.annotations.Get;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by chriswoodrow on 23/11/2015.
 */
public abstract class DataEndPoint<T extends ElasticTypedObject> {
    @Get("/all")
    public final List<T> getAll(){
        return ElasticRepository.INSTANCE.all(getParametrizedType());
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocResult<T>> find(double lat, double lon, int size){
        return ElasticRepository.INSTANCE.find(lat, lon, size, getParametrizedType());
    }

    private Class<T> getParametrizedType(){
        return (Class<T>) ReflexiveUtils.getParametrizedType(getClass());
    }
}