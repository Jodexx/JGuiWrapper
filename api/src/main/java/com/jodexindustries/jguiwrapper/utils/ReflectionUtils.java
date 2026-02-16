package com.jodexindustries.jguiwrapper.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unused")
public class ReflectionUtils {
    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType pt) {
                Type arg = pt.getActualTypeArguments()[index];
                if (arg instanceof Class<?>) {
                    return (Class<?>) arg;
                }
            }
        }
        throw new IllegalStateException("Type argument not found");
    }
}
