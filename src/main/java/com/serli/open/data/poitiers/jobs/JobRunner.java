package com.serli.open.data.poitiers.jobs;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by chriswoodrow on 25/11/2015.
 */
public abstract class JobRunner {
    private JobRunner(){}

    public static <T extends Job>  void run(Class<T> clazz){
        try {
            T job = clazz.getConstructor().newInstance();
            job.run();
            
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
           throw new RuntimeException(e);
        }
    }
}
