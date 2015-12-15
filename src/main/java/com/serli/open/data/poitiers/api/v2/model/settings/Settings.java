package com.serli.open.data.poitiers.api.v2.model.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
public class Settings {
    public String dashboardURL;
    public List<APIRoutes> routes = new ArrayList<>();
    public Map<String, DataSource> sources = new HashMap<>();

}
