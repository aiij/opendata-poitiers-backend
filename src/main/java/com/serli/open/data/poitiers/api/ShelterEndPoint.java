package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.model.GeolocResult;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import com.serli.open.data.poitiers.api.model.Shelter;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

import java.util.List;

/**
 * Created by chris on 04/05/15.
 */
@Prefix("bike-shelters")
public class ShelterEndPoint extends DataEndPoint<Shelter>{
}
