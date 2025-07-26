package com.jodexindustries.jguiwrapper.api.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a graphical user interface (GUI) instance.
 * Provides methods for opening, closing, and updating GUI holders for players.
 * Also manages active GUI instances for tracking and mass operations.
 */
@SuppressWarnings({"unused"})
public interface Gui {
    /**
     * Set of all active GUI instances, stored as weak references to prevent memory leaks.
     */
    Set<WeakReference<Gui>> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Gets the holder for this GUI instance.
     * @return the GuiHolder associated with this GUI
     */
    @NotNull GuiHolder holder();

    /**
     * Updates the holder for this GUI instance. Experimental API.
     */
    @ApiStatus.Experimental
    void updateHolder();

    /**
     * Opens this GUI for the specified player.
     * @param player the player to open the GUI for
     */
    void open(@NotNull HumanEntity player);

    /**
     * Opens this GUI for the specified player with a custom title.
     * @param player the player to open the GUI for
     * @param title the custom title to use
     */
    void open(@NotNull HumanEntity player, Component title);

    /**
     * Closes this GUI for the specified player.
     * @param player the player to close the GUI for
     */
    void close(@NotNull HumanEntity player);

    /**
     * Closes this GUI for all current viewers.
     */
    default void close() {
        new ArrayList<>(holder().getInventory().getViewers()).forEach(this::close);
    }

    /**
     * Returns a set of all currently active GUI instances.
     * @return set of active GUI instances
     */
    static Set<Gui> getActiveInstances() {
        Set<Gui> active = new HashSet<>();
        for (WeakReference<Gui> ref : INSTANCES) {
            Gui instance = ref.get();
            if (instance != null) {
                active.add(instance);
            }
        }
        return active;
    }
}
