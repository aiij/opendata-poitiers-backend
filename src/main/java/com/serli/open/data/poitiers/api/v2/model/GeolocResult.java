package com.serli.open.data.poitiers.api.v2.model;

/**
 * Created by chris on 14/11/15.
 */
public class GeolocResult<T> {
    public GeolocResult(T pointOfInterest, Integer distanceInMeters) {
        this.pointOfInterest = pointOfInterest;
        this.distanceInMeters = distanceInMeters;
    }

    public final T pointOfInterest;
    public final Integer distanceInMeters;
}
