package com.serli.open.data.poitiers.bike.shelters.repository;

import com.serli.open.data.poitiers.bike.shelters.rest.model.Park;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Service;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Shelter;
import com.serli.open.data.poitiers.bike.shelters.rest.model.Ticketmachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 04/05/15.
 */
public class InMemoryRepository {
    private static List<Shelter> shelters = new ArrayList<>();
    private static List<Ticketmachine> ticketmachines = new ArrayList<>();
    private static List<Park> parks = new ArrayList<>();
    private static List<Service> services = new ArrayList<>();

    public static void add(Shelter shelter){
        shelters.add(shelter);
    }
    
    public static void add(Ticketmachine ticketmachine){
        ticketmachines.add(ticketmachine);
    }
    
    public static void add(Park park){
        parks.add(park);
    }
    
    public static void add(Service service){
        services.add(service);
    }

    public static List<Shelter> allShelters(){
        return shelters;
    }
    
    public static List<Ticketmachine> allTicketmachines(){
        return ticketmachines;
    }
    
    public static List<Park> allParks(){
        return parks;
    }
    
    public static List<Service> allService(){
        return services;
    }

}
