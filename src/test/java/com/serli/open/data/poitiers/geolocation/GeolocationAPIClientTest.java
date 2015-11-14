package com.serli.open.data.poitiers.geolocation;

import org.junit.Assert;
import org.junit.Test;

import static com.serli.open.data.poitiers.geolocation.GeolocationAPIClient.geocode;
import static com.serli.open.data.poitiers.geolocation.GeolocationAPIClient.reverseGeoCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by chris on 14/11/15.
 */
public class GeolocationAPIClientTest {
    @Test
    public void testGeocode() {
        assertEquals(new LatLon(46.591725, 0.341868),
                geocode("23 rue de rochereuil Poitiers"));
    }

    @Test
    public void testGeocode_multi() {
        assertEquals(new LatLon(46.579729, 0.334224),
                geocode("Poitiers"));
    }

    @Test
    public void testGeocode_none() {
        assertNull(geocode("zfzefzefefertrgrthrhrt"));

    }

    @Test
    public void testReverseGeoCode(){
        assertEquals(new Address("23 Rue de Rochereuil", "86000", "Poitiers"),
                reverseGeoCode(new LatLon(46.591725, 0.341868)));
    }

    @Test
    public void testReverseGeoCode_null(){
        assertNull(reverseGeoCode(new LatLon(1000, 1000)));
    }

}
