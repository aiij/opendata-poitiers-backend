/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.api.model;

import com.serli.open.data.poitiers.repository.ElasticRepository;
import java.util.Arrays;

/**
 *
 * @author dupar_000
 */
public class GlassContainer implements ElasticTypedObject{
    
    public final int numeroBorne;
    public final int volume;
    public final String freq_collect;
    public final String observation;
    public final String jour_collect;
    public final double[] location;
    public final int objectId;
    public final String address;

    
    public GlassContainer(int numeroBorne, int volume, String freq_collect, String observation, String jour_collect, double[] location, int objectId, String address) {
        this.numeroBorne = numeroBorne;
        this.volume = volume;
        this.freq_collect = freq_collect;
        this.observation = observation;
        this.jour_collect = jour_collect;
        this.location = location;
        this.objectId = objectId;
        this.address = address;
    }
    
    public GlassContainer(){
        this.numeroBorne = -1;
        this.volume = -1;
        this.freq_collect = null;
        this.observation = null;
        this.jour_collect = null;
        this.location = null;
        this.objectId = -1;
        this.address = null;
    }
    
    @Override
    public String toString() {
        return "Container{" +
                ", num_born=" + numeroBorne +
                ", volume=" + volume +
                 ", freq_collect=" + freq_collect +
                 ", observation=" + observation +
                 ", jour_collect=" + jour_collect +
                ", location=" + Arrays.toString(location) +
                ", objectId=" + objectId +
                '}';
    }
    
    @Override
    public String getElasticType() {
        return ElasticRepository.GLASS_CONTAINER_TYPE;
    }
}
