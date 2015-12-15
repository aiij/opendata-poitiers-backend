package com.serli.open.data.poitiers.utils;

import com.google.common.base.Preconditions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by chriswoodrow on 24/11/2015.
 */
public class ReflexiveUtils {
    public static Class<?> getParametrizedType(Class<?> clazz) {
        checkArgument(clazz != null);

        Type genericSuperclass = clazz.getGenericSuperclass();
        checkArgument(genericSuperclass instanceof ParameterizedType, "Class : " + clazz + " is not parametrized");

        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                .getActualTypeArguments();

        return (Class<?>) actualTypeArguments[0];
    }
}
