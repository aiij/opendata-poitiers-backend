package com.serli.open.data.poitiers.api.v1.model;

/**
 * Created by chris on 14/11/15.
 */
public class GeolocResultV1<T> {
    public GeolocResultV1(T pointOfInterest, Integer distanceInMeters) {
        this.shelter = pointOfInterest;
        this.distanceInMeters = distanceInMeters;
    }

    public final T shelter;
    public final Integer distanceInMeters;
}
