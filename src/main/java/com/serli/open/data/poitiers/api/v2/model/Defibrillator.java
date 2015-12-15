/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.api.v2.model;

import java.util.Arrays;

/**
 *
 * @author Julien L
 */
public class Defibrillator {
    public final String identifier;
    public final double[] location;
    public final int objectId;
    public final String town;
    public final String place;
    public final int addressNumber;
    public final String address;
    public final String accessCondition;

    public Defibrillator(String identifier, double[] location, int objectId, String town, String place, int addressNumber, String address, String accessCondition) {
        this.identifier = identifier;
        this.location = location;
        this.objectId = objectId;
        this.town = town;
        this.place = place;
        this.addressNumber = addressNumber;
        this.address = address;
        this.accessCondition = accessCondition;
    }

    @Override
    public String toString() {
        return "Defibrillator{" +
                ", identifier=" + identifier +
                ", location=" + Arrays.toString(location) +
                ", objectId=" + objectId +
                ", town=" + town +
                ", place=" + place +
                ", addressNumber=" + addressNumber +
                ", address=" + address +
                ", accesCondition=" + accessCondition +
                '}';
    }
}
