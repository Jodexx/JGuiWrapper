package com.jodexindustries.jguiwrapper.nms;

import com.jodexindustries.jguiwrapper.api.nms.NMSUtils;
import org.bukkit.entity.HumanEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NMSUtilsImpl implements NMSUtils {

    private Method GET_SERVER_PLAYER_METHOD;

    public NMSUtilsImpl() {
        try {
            GET_SERVER_PLAYER_METHOD = HumanEntity.class.getDeclaredMethod("getHandle");
        } catch (NoSuchMethodException ignored) {
        }
    }

    public Object getServerPlayer(HumanEntity player) {
        return invokeInstance(GET_SERVER_PLAYER_METHOD, player);
    }

    public Object getField(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (Throwable e) {
            return null;
        }
    }

    public Object invokeInstance(Method method, Object instance, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (Throwable t) {
            return null;
        }
    }

    public Object newInstance(Constructor<?> constructor, Object ... initargs) {
        try {
            return constructor.newInstance(initargs);
        } catch (Throwable t) {
            return null;
        }
    }

    public Method getDeclaredMethod(Class<?> clazz, String method, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(method, parameterTypes);
        } catch (Throwable e) {
            return null;
        }
    }
}
