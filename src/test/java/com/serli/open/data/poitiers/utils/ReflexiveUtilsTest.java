package com.serli.open.data.poitiers.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static com.serli.open.data.poitiers.utils.ReflexiveUtils.getParametrizedType;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
public class ReflexiveUtilsTest {
    @Test(expected = IllegalArgumentException.class)
    public void testGetParametrizedType_null(){
        getParametrizedType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParametrizedType_notParametrizedType(){
        getParametrizedType(ReflexiveUtilsTest.class);
    }

    @Test
    public void testGetParametrizedType(){
        Assert.assertEquals(String.class,getParametrizedType(Parametrized.class));
    }



    private class Parametrized extends ArrayList<String> {}
}
