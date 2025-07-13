package com.jodexindustries.jguiwrapper.api.nms;

import org.bukkit.entity.HumanEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface NMSUtils {

    Object getServerPlayer(HumanEntity player);

    Object getField(Field field, Object obj);

    Object invokeInstance(Method method, Object instance, Object... params);

    Object newInstance(Constructor<?> constructor, Object ... initargs);
}
