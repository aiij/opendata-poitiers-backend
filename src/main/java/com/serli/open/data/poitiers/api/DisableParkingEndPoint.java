package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.api.model.DisabledParking;
import com.serli.open.data.poitiers.api.model.GeolocResult;
import com.serli.open.data.poitiers.api.model.Shelter;
import com.serli.open.data.poitiers.repository.ElasticRepository;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;

import java.util.List;

/**
 * Created by chris on 16/11/15.
 */
@Prefix("disabled-parkings")
public class DisableParkingEndPoint extends DataEndPoint<DisabledParking> {
}
