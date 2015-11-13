package com.serli.open.data.poitiers.utils;

/**
 * Created by chris on 13/11/15.
 */
public abstract class EnvUtils {
    private EnvUtils(){}

    public static String getEnvOrDefault(String name, String defaultValue){
        String env = System.getenv(name);
        return env != null ? env : defaultValue;
    }
}
