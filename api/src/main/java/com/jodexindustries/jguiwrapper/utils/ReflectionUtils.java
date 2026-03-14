package com.jodexindustries.jguiwrapper.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

@SuppressWarnings("unused")
public class ReflectionUtils {
    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        Class<?> resolved = getGenericClassOrNull(clazz, index);
        if (resolved != null) {
            return resolved;
        }
        throw new IllegalStateException("Type argument not found");
    }

    private static Class<?> getGenericClassOrNull(Class<?> clazz, int index) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            Class<?> resolved = resolveFromType(current.getGenericSuperclass(), index);
            if (resolved != null) {
                return resolved;
            }

            for (Type type : current.getGenericInterfaces()) {
                resolved = resolveFromType(type, index);
                if (resolved != null) {
                    return resolved;
                }
            }

            current = current.getSuperclass();
        }
        return null;
    }

    private static Class<?> resolveFromType(Type type, int index) {
        if (type instanceof ParameterizedType pt) {
            Type arg = pt.getActualTypeArguments()[index];
            return extractClass(arg);
        }
        if (type instanceof Class<?> cls) {
            return getGenericClassOrNull(cls, index);
        }
        return null;
    }

    private static Class<?> extractClass(Type type) {
        if (type instanceof Class<?> cls) {
            return cls;
        }
        if (type instanceof ParameterizedType pt) {
            Type raw = pt.getRawType();
            if (raw instanceof Class<?> cls) {
                return cls;
            }
        }
        if (type instanceof TypeVariable<?> tv) {
            for (Type bound : tv.getBounds()) {
                Class<?> cls = extractClass(bound);
                if (cls != null) {
                    return cls;
                }
            }
        }
        return null;
    }
}
