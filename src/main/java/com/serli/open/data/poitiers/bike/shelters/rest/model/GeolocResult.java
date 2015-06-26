package com.serli.open.data.poitiers.bike.shelters.rest.model;

/**
 * Created by chris on 04/05/15.
 */
public class GeolocResult {
    public GeolocResult(Shelter shelter, Integer distanceInMeters) {
        this.shelter = shelter;
        this.distanceInMeters = distanceInMeters;
    }
    
    public GeolocResult(Park park, Integer distanceInMeters) {
        this.park = park;
        this.distanceInMeters = distanceInMeters;
    }
    
    public GeolocResult(Ticketmachine ticketmachine, Integer distanceInMeters) {
        this.ticketmachine = ticketmachine;
        this.distanceInMeters = distanceInMeters;
    }

    public Shelter shelter;
    public Park park;
    public Ticketmachine ticketmachine;
    public Integer distanceInMeters;
}
