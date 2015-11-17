package com.serli.open.data.poitiers.api.model;

/**
 * Created by chris on 14/11/15.
 */
public class GeolocResult<T> {
    public GeolocResult(T pointOfInterest, Integer distanceInMeters) {
        this.pointOfInterest = pointOfInterest;
        this.shelter = pointOfInterest;
        this.distanceInMeters = distanceInMeters;
    }

    // FIXME for backward compatibility (to remove when iOS app is ready...)
    public final T shelter;

    public final T pointOfInterest;
    public final Integer distanceInMeters;
}
