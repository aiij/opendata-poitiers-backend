package com.serli.open.data.poitiers.bike.shelters.rest;

import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Gets;
import net.codestory.http.annotations.Prefix;

/**
 * Created by chris on 13/11/15.
 */
@Prefix("admin")
public class AdminEndPoint {

    @Gets({@Get("/"), @Get()})
    public String adminHome(){
        return "<h1>Admin</h1>";
    }
}
