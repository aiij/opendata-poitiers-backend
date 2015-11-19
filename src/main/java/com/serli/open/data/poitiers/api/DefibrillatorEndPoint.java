/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.model.GeolocResult;
import com.serli.open.data.poitiers.api.model.Defibrillator;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import java.util.List;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

/**
 *
 * @author Julien L
 */
@Prefix("defibrillators")
public class DefibrillatorEndPoint {
    @Get("/all")
    public List<Defibrillator> all(){
        return ElasticRepository.INSTANCE.all(Defibrillator.class);
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocResult<Defibrillator>> find(double lat, double lon, int size){
        return ElasticRepository.INSTANCE.find(lat, lon, size, Defibrillator.class);
    }
}
