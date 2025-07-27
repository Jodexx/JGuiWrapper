package com.jodexindustries.jguiwrapper.api.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum SerializerType {

    LEGACY("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer", "legacyAmpersand"),

    MINI_MESSAGE("net.kyori.adventure.text.minimessage.MiniMessage", "miniMessage");

    ComponentSerializer<Component, Component, String> serializer;

    @SuppressWarnings("unchecked")
    SerializerType(String name, String method) {
        try {
            Class<?> clazz = Class.forName(name);
            Method declaredMethod = clazz.getDeclaredMethod(method);
            serializer = (ComponentSerializer<Component, Component, String>) declaredMethod.invoke(null);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | ClassCastException |
                 InvocationTargetException ignored) {
        }
    }

    @NotNull
    public Component deserialize(@Nullable String string) {
        if (string == null || serializer == null) return Component.empty();

        return serializer.deserialize(string);
    }

    @Nullable
    public String serialize(@NotNull Component component) {
        if (serializer == null) return null;

        return serializer.serialize(component);
    }

    @Nullable
    public ComponentSerializer<Component, Component, String> serializer() {
        return serializer;
    }
}
