package com.serli.open.data.poitiers.api;

import com.serli.open.data.poitiers.jobs.ImportBikeSheltersDataJob;
import com.serli.open.data.poitiers.jobs.ImportDisabledParkingsDataJob;
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

    @Get("/reload/bike-shelters")
    public void reloadShelters(){
        new ImportBikeSheltersDataJob().createIndexAndLoad();

    }

    @Get("/reload/disabled-parkings")
    public void disabledParkings(){
        new ImportDisabledParkingsDataJob().createIndexAndLoad();

    }

    @Get("/reload/all")
    public void reloadAll(){
        new ImportBikeSheltersDataJob().createIndexAndLoad();
        new ImportDisabledParkingsDataJob().createIndexAndLoad();
    }

}
