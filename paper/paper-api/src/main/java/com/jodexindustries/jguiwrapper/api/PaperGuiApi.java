package com.jodexindustries.jguiwrapper.api;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.nms.NMSWrapper;
import com.jodexindustries.jguiwrapper.api.placeholder.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.registry.GlobalRegistry;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Main entry point for the JGuiWrapper API.
 * <p>
 * Provides access to core components such as the global registry, plugin instance, NMS wrapper, and GUI management utilities.
 * This class is intended to be used as a singleton and should be accessed via {@link #get()}.
 */
@SuppressWarnings({"unused"})
public abstract class PaperGuiApi extends GuiApi {

    protected final Plugin plugin;

    protected PaperGuiApi(Plugin plugin) {
        super(plugin::getLogger);

        this.plugin = plugin;
    }

    /**
     * Gets the singleton instance of the API.
     *
     * @return the PaperGuiApi instance
     * @throws IllegalStateException if the instance has not been set yet
     */
    @NotNull
    public static PaperGuiApi get() {
        if (instance == null || !(instance instanceof PaperGuiApi)) throw new IllegalStateException("GuiApi instance has not been set yet.");
        return (PaperGuiApi) instance;
    }

    /**
     * Gets the singleton instance of the API, if it has been initialized.
     * <p>
     * Unlike {@link #get()}, this method does not throw an exception when the instance
     * is not yet set. Instead, it returns an {@link Optional} which will be empty
     * if the API has not been initialized.
     * </p>
     *
     * <pre>{@code
     * PaperGuiApi.getOptional().ifPresent(api -> {
     *     // Safe access to the API
     *     Plugin plugin = api.getPlugin();
     * });
     * }</pre>
     *
     * @return an {@link Optional} containing the API instance if initialized,
     * or an empty optional if not
     */
    @NotNull
    public static Optional<PaperGuiApi> getOptional() {
        if (!(instance instanceof PaperGuiApi)) return Optional.empty();

        return Optional.ofNullable(((PaperGuiApi) instance));
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
    public Plugin getPlugin() {
        return this.plugin;
    }

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
}
