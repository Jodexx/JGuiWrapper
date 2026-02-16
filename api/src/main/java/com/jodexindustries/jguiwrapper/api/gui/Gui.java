package com.jodexindustries.jguiwrapper.api.gui;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents a graphical user interface (GUI) instance.
 */
public interface Gui {

    /**
     * Set of all active GUI instances, stored as weak references to prevent memory leaks.
     */
    Set<WeakReference<Gui>> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @NotNull
    GuiHolder holder();

    /**
     * Updates the holder for this GUI instance and reopens the GUI for all viewers.
     */
    @ApiStatus.Experimental
    void updateHolder();

    /**
     * Opens this GUI for the specified player.
     *
     * @param player the player to open the GUI for
     */
    void open(@NotNull Object player);

    /**
     * Opens this GUI for the specified player with a custom title.
     *
     * @param player the player to open the GUI for
     * @param title  the custom title to use
     */
    void open(@NotNull Object player, @NotNull Component title);

    /**
     * Closes this GUI for the specified player.
     *
     * @param player the player to close the GUI for
     */
    void close(@NotNull Object player);

    /**
     * Closes this GUI for all current viewers.
     */
    void close();

    /**
     * Returns a set of all currently active GUI instances.
     *
     * @return set of active GUI instances
     */
    @NotNull
    static Set<Gui> getActiveInstances() {
        return INSTANCES
                .stream()
                .map(Reference::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
