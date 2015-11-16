package com.serli.open.data.poitiers.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chris on 14/11/15.
 */
public class GeolocResult<T> {
    public GeolocResult(T result, Integer distanceInMeters) {
        this.result = result;
        this.shelter = result;
        this.distanceInMeters = distanceInMeters;
    }

    // FIXME for backward compatibility (to remove)
    public final T shelter;
    public final T result;
    public final Integer distanceInMeters;
}
