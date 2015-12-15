/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.api.v2.model;

import java.util.Arrays;

/**
 *
 * @author dupar_000
 */
public class GlassContainer{
    
    public final int containerNumber;
    public final int capacity;
    public final String collectionFrequency;
    public final String observation;
    public final String collectionDay;
    public final double[] location;
    public final int objectId;
    public final String address;

    
    public GlassContainer(int containerNumber, int capacity, String collectionFrequency, String observation, String collectionDay, double[] location, int objectId, String address) {
        this.containerNumber = containerNumber;
        this.capacity = capacity;
        this.collectionFrequency = collectionFrequency;
        this.observation = observation;
        this.collectionDay = collectionDay;
        this.location = location;
        this.objectId = objectId;
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "Container{" +
                ", num_born=" + containerNumber +
                ", capacity=" + capacity +
                 ", collectionFrequency=" + collectionFrequency +
                 ", observation=" + observation +
                 ", collectionDay=" + collectionDay +
                ", location=" + Arrays.toString(location) +
                ", objectId=" + objectId +
                '}';
    }
}
