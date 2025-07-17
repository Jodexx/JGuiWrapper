package com.jodexindustries.jguiwrapper.common;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GuiUtils {

    @Nullable
    public static GuiHolder getHolder(Inventory inventory) {
        if (inventory == null) return null;

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) return null;

        return holder instanceof GuiHolder ? ((GuiHolder) holder) : null;
    }

    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                Type arg = pt.getActualTypeArguments()[index];
                if (arg instanceof Class<?>) {
                    return (Class<?>) arg;
                }
            }
        }
        throw new IllegalStateException("Type argument not found");
    }
}
