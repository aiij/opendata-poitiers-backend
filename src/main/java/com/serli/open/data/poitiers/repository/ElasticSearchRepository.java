package com.serli.open.data.poitiers.repository;

import com.serli.open.data.poitiers.elasticsearch.ElasticUtils;
import com.serli.open.data.poitiers.elasticsearch.RuntimeJestClient;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
public abstract class ElasticSearchRepository {
    public static final String OPEN_DATA_POITIERS_INDEX = "open-data-poitiers";
    protected final RuntimeJestClient client ;

    public ElasticSearchRepository() {
        client = ElasticUtils.createClient();
    }
}
