package com.serli.open.data.poitiers.api.model;

/**
 * Created by chris on 04/05/15.
 */
public class GeolocShelterResult {
    public GeolocShelterResult(Shelter shelter, Integer distanceInMeters) {
        this.shelter = shelter;
        this.distanceInMeters = distanceInMeters;
    }

    public final Shelter shelter;
    public final Integer distanceInMeters;
}
