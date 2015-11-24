package com.serli.open.data.poitiers.jobs;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.junit.Ignore;

/**
 * Created by chris on 17/11/15.
 */
public class ImportBikeSheltersDataJobTest {
    @Test
    public void testCapitalize() {
        assertEquals("Site appui velo", StringUtils.capitalize(StringUtils.lowerCase("SIte appui velo")));
    }
}
