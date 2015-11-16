package com.serli.open.data.poitiers.api.model;

import com.serli.open.data.poitiers.repository.ElasticRepository;

import java.util.Arrays;

/**
 * Created by chris on 04/05/15.
 */
public class Shelter implements ElasticTypedObject {
    public final String type;
    public final Integer capacity;
    public final double[] location;
    public final int objectId;
    public final String address;

    public Shelter(String type, Integer capacity, double[] location, int objectId, String address) {
        this.type = type;
        this.capacity = capacity;
        this.location = location;
        this.objectId = objectId;
        this.address = address;
    }

    private Shelter() {
        type = null;
        capacity = null;
        location = null;
        objectId = -1;
        address = null;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "type='" + type + '\'' +
                ", capacity=" + capacity +
                ", location=" + Arrays.toString(location) +
                ", objectId=" + objectId +
                '}';
    }

    @Override
    public String getElasticType() {
        return ElasticRepository.BIKE_SHELTERS_TYPE;
    }
}
