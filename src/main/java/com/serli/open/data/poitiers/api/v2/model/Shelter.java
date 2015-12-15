package com.serli.open.data.poitiers.api.v2.model;

import java.util.Arrays;

/**
 * Created by chris on 04/05/15.
 */
public class Shelter {
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

    @Override
    public String toString() {
        return "Shelter{" +
                "type='" + type + '\'' +
                ", capacity=" + capacity +
                ", location=" + Arrays.toString(location) +
                ", objectId=" + objectId +
                '}';
    }
}
