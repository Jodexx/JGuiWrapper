package com.jodexindustries.jguiwrapper.api.text;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents different {@link ComponentSerializer} types that can be used to serialize and
 * deserialize {@link Component} objects.
 * <p>
 * Each constant corresponds to a particular Adventure serializer implementation.
 * If the underlying serializer class cannot be found on the classpath,
 * the serializer will be {@code null} and calls to {@link #serialize(Component)} will return {@code null},
 * while {@link #deserialize(String)} will return {@link Component#empty()}.
 * </p>
 */
@SuppressWarnings({"unused"})
public enum SerializerType implements ComponentSerializer<Component, Component, String> {

    /**
     * Legacy serializer using the {@code &}-based formatting codes.
     * <p>
     * Uses {@code LegacyComponentSerializer.legacyAmpersand()}.
     * </p>
     */
    LEGACY_AMPERSAND("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer", "legacyAmpersand"),

    /**
     * Legacy serializer using the {@code §}-based formatting codes.
     * <p>
     * Uses {@code LegacyComponentSerializer.legacySection()}.
     * </p>
     */
    LEGACY_SECTION("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer", "legacySection"),

    /**
     * @deprecated This constant is obsolete. Use {@link #LEGACY_AMPERSAND} instead.
     */
    @Deprecated
    LEGACY(LEGACY_AMPERSAND.serializer),

    /**
     * MiniMessage serializer.
     * <p>
     * Uses {@code MiniMessage.miniMessage()}.
     * </p>
     */
    MINI_MESSAGE("net.kyori.adventure.text.minimessage.MiniMessage", "miniMessage"),

    /**
     * Plain text serializer.
     * <p>
     * Uses {@code PlainComponentSerializer.plain()}.
     * </p>
     */
    PLAIN("net.kyori.adventure.text.serializer.plain.PlainComponentSerializer", "plain"),

    /**
     * JSON serializer
     * Uses {@code JSONComponentSerializer.json()}.
     */
    JSON("net.kyori.adventure.text.serializer.json.JSONComponentSerializer", "json"),

    /**
     * GSON serializer
     * Uses {@code GsonComponentSerializer.gson()}.
     */
    GSON("net.kyori.adventure.text.serializer.gson.GsonComponentSerializer", "gson");

    ComponentSerializer<Component, Component, String> serializer;

    volatile boolean informed;

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

    SerializerType(ComponentSerializer<Component, Component, String> serializer) {
        this.serializer = serializer;
    }

    /**
     * Deserializes a string into a {@link Component}.
     *
     * @param string the string to deserialize
     * @return the deserialized component, or {@link Component#empty()} if the serializer is unavailable
     */
    @NotNull
    @Override
    public Component deserialize(@NotNull String string) {
        if (serializer == null) {
            warnOnce(string);
            return Component.empty();
        }
        return serializer.deserialize(string);
    }

    /**
     * Serializes a {@link Component} into a string.
     *
     * @param component the component to serialize
     * @return the serialized string, or an empty string ("") if the serializer is unavailable
     */
    @NotNull
    @Override
    public String serialize(@NotNull Component component) {
        if (serializer == null) {
            warnOnce(component);
            return "";
        }

        return serializer.serialize(component);
    }

    /**
     * Returns the underlying serializer instance for this type.
     *
     * @return the serializer instance, or {@code null} if unavailable
     */
    @Nullable
    public ComponentSerializer<Component, Component, String> serializer() {
        return serializer;
    }

    private void warnOnce(Object input) {
        if (!informed) {
            GuiApi.getOptional().ifPresent(api -> {
                api.getPlugin().getLogger().warning("Serializer: '" + name() + "' not found! Couldn't serialize: " + input);
                informed = true;
            });
        }
    }
}
