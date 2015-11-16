package com.serli.open.data.poitiers.api.model;

import com.serli.open.data.poitiers.repository.ElasticRepository;

import java.util.Arrays;

/**
 * Created by chris on 14/11/15.
 */
public class DisabledParking implements ElasticTypedObject {
    public final int objectId;
    public final String identifier;
    public final String district;
    public final String state;
    public final String comment;
    public final String town;
    public final double[] location;

    public DisabledParking(int objectId, String identifier, String district, String state, String comment, String town, double[] location) {
        this.objectId = objectId;
        this.identifier = identifier;
        this.district = district;
        this.state = state;
        this.comment = comment;
        this.town = town;
        this.location = location;
    }

    private DisabledParking(){
        this.objectId = -1;
        this.identifier = null;
        this.district = null;
        this.state = null;
        this.comment = null;
        this.town = null;
        this.location = null;
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
                ", location=" + Arrays.toString(location) +
                '}';
    }

    @Override
    public String getElasticType() {
        return ElasticRepository.DISABLED_PARKING_TYPE;
    }
}
