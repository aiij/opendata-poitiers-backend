/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.model.GeolocResult;
import com.serli.open.data.poitiers.api.model.GlassContainer;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import java.util.List;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

/**
 *
 * @author dupar_000
 */
@Prefix("glass-container")
public class GlassContainerEndPoint {
   @Get("/all")
    public List<GlassContainer> all(){
        return ElasticRepository.INSTANCE.all(GlassContainer.class);
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocResult<GlassContainer>> find(double lat, double lon, int size){
        return ElasticRepository.INSTANCE.find(lat, lon, size, GlassContainer.class);
    }
}
