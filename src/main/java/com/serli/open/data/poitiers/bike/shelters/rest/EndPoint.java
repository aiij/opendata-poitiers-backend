package com.serli.open.data.poitiers.bike.shelters.rest;

import com.serli.open.data.poitiers.bike.shelters.repository.ElasticRepository;
import com.serli.open.data.poitiers.bike.shelters.repository.InMemoryRepository;
import com.serli.open.data.poitiers.bike.shelters.rest.model.GeolocResult;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Park;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Service;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Shelter;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Ticketmachine;

import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Gets;
import net.codestory.http.annotations.Prefix;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chris on 04/05/15.
 */
@Prefix("allInstallations")
public class EndPoint {


    @Get("/")
    public String home(){
        return "<p>GET <a href=\"allShelters\">all Shelters</a> : all shelters</p>" +
        	   "<p>GET <a href=\"allTicketmachines\">all Ticketmachines</a> : all ticketmachines</p>" +
        	   "<p>GET <a href=\"allParks\">all Parks</a> : all parks</p>" +
        	   "<p>GET <a href=\"allServices\">all Services</a> : all services</p>" +
               "<p>GET <a href=\"find?lat=46.578636&lon=0.337959\">find?lat=:lat&lon=:lon&size=:size</a> : search closest shelters from lar/lon point, size is optional</p>";
    }

    @Get("/allShelters")
    public List<Shelter> allShelters(){
        return ElasticRepository.INSTANCE.allShelters();
    }
    
    @Get("/allTicketmachines")
    public List<Ticketmachine> allTicketmachines(){
        return ElasticRepository.INSTANCE.allTicketmachines();
    }
    
    @Get("/allParks")
    public List<Park> allParks(){
        return ElasticRepository.INSTANCE.allParks();
    }
    
    @Get("/allServices")
    public List<Service> allService(){
    	return ElasticRepository.INSTANCE.allService();
    }

    @Get("/find?lat=:lat&lon=:lon&size=:size")
    public List<GeolocResult> find(double lat, double lon, int size){
        return ElasticRepository.INSTANCE.find(lat, lon, size);
    }

}
