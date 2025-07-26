package com.jodexindustries.jguiwrapper.api;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
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
    protected static synchronized void setInstance(GuiApi api) {
        if (GuiApi.instance != null) throw new IllegalStateException("GuiApi instance is already set.");
        GuiApi.instance = api;
    }

    /**
     * Gets the singleton instance of the API.
     *
     * @return the GuiApi instance
     * @throws IllegalStateException if the instance has not been set yet
     */
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
}
