package com.serli.open.data.poitiers.api.v2.model;

import java.util.Arrays;

/**
 * Created by chris on 14/11/15.
 */
public class DisabledParking{
    public final int objectId;
    public final String identifier;
    public final String district;
    public final String state;
    public final String comment;
    public final String town;
    public final String address;
    public final double[] location;

    public DisabledParking(int objectId, String identifier, String district, String state, String comment, String town, String address, double[] location) {
        this.objectId = objectId;
        this.identifier = identifier;
        this.district = district;
        this.state = state;
        this.comment = comment;
        this.town = town;
        this.address = address;
        this.location = location;
    }

    @Override
    public String toString() {
        return "DisabledParking{" +
                "objectId=" + objectId +
                ", identifier='" + identifier + '\'' +
                ", district='" + district + '\'' +
                ", state='" + state + '\'' +
                ", comment='" + comment + '\'' +
                ", town='" + town + '\'' +
                ", address='" + address + '\'' +
                ", location=" + Arrays.toString(location) +
                '}';
    }
}
