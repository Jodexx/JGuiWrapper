package com.jodexindustries.jguiwrapper.api;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.api.placeholder.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Main entry point for the JGuiWrapper API.
 * <p>
 * Provides access to core components such as the global registry, plugin instance, NMS wrapper, and GUI management utilities.
 * This class is intended to be used as a singleton and should be accessed via {@link #get()}.
 */
@SuppressWarnings({"unused"})
public abstract class GuiApi {

    private static volatile GuiApi instance = null;

    @ApiStatus.Internal
    protected static synchronized void setInstance(@NotNull GuiApi api) {
        if (GuiApi.instance != null) throw new IllegalStateException("GuiApi instance is already set.");
        GuiApi.instance = api;
    }

    /**
     * Gets the singleton instance of the API.
     *
     * @return the GuiApi instance
     * @throws IllegalStateException if the instance has not been set yet
     */
    @NotNull
    public static GuiApi get() {
        if (GuiApi.instance == null) throw new IllegalStateException("GuiApi instance has not been set yet.");
        return instance;
    }

    /**
     * Gets the global registry for data and handler management.
     *
     * @return the global registry
     */
    @NotNull
    public abstract GlobalRegistry getRegistry();

    /**
     * Gets the plugin instance associated with this API.
     *
     * @return the plugin instance
     */
    @NotNull
    public abstract Plugin getPlugin();

    /**
     * Gets the NMS wrapper for version-dependent operations.
     *
     * @return the NMS wrapper
     */
    @NotNull
    public abstract NMSWrapper getNMSWrapper();

    /**
     * Gets the currently opened GUI holder for the specified player, or null if none is open.
     *
     * @param player the player to check
     * @return the opened GuiHolder, or null if none is open
     */
    @Nullable
    public abstract GuiHolder getOpenedGui(@NotNull Player player);

    /**
     * Creates a new PlaceholderEngine
     *
     * @return new PlaceholderEngine instance
     */
    @NotNull
    public abstract PlaceholderEngine createPlaceholderEngine();

    /**
     * Checks whether the PlaceholderAPI plugin is loaded and available on the server.
     * <p>
     * If PlaceholderAPI is present and enabled, this method returns {@code true},
     * otherwise it returns {@code false}.
     * </p>
     *
     * @return {@code true} if PlaceholderAPI is loaded, {@code false} otherwise
     */
    @Contract(pure = true)
    public abstract boolean isPAPI();

    /**
     * Returns the default {@link SerializerType} used by the API.
     * <p>
     * By default, this value is set to {@link SerializerType#LEGACY_AMPERSAND}, but it may be
     * changed at runtime using {@link #defaultSerializer(SerializerType)}.
     * </p>
     *
     * @return the default {@link SerializerType}, never {@code null}
     */
    @ApiStatus.AvailableSince("1.0.0.5")
    @NotNull
    public abstract SerializerType defaultSerializer();

    /**
     * Sets the default {@link SerializerType} to be used by the API.
     * <p>
     * This will affect all future operations that rely on the default serializer,
     * unless explicitly overridden.
     * </p>
     *
     * @param serializerType the new default {@link SerializerType}, must not be {@code null}
     */
    @ApiStatus.AvailableSince("1.0.0.5")
    public abstract void defaultSerializer(@NotNull SerializerType serializerType);
}
