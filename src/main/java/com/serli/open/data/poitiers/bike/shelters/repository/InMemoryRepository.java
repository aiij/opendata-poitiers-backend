package com.serli.open.data.poitiers.bike.shelters.repository;

import com.serli.open.data.poitiers.bike.shelters.rest.model.Shelter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 04/05/15.
 */
public class InMemoryRepository {
    private static List<Shelter> shelters = new ArrayList<>();

    public static void add(Shelter shelter){
        shelters.add(shelter);
    }

    public static List<Shelter> all(){
        return shelters;
    }

}
