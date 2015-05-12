package com.serli.open.data.poitiers.bike.shelters.rest.model;

/**
 * Created by chris on 04/05/15.
 */
public class GeolocShelterResult {
    public GeolocShelterResult(Shelter shelter, Integer distanceInMeters) {
        this.shelter = shelter;
        this.distanceInMeters = distanceInMeters;
    }

    public Shelter shelter;
    public Integer distanceInMeters;
}
