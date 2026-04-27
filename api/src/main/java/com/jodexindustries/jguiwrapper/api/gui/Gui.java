package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.api.user.User;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents a graphical user interface (GUI) instance.
 * Provides methods for opening, closing, and updating GUI holders for players.
 */
public interface Gui {

    /**
     * Set of all active GUI instances, stored as weak references to prevent memory leaks.
     */
    Set<WeakReference<Gui>> INSTANCES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Gets the holder for this GUI instance.
     *
     * @return the GuiHolder associated with this GUI
     */
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
     * @param user the player to open the GUI for
     */
    void open(@NotNull User user);

    /**
     * Opens this GUI for the specified player with a custom title.
     *
     * @param user the player to open the GUI for
     * @param title  the custom title to use
     */
    void open(@NotNull User user, @NotNull Component title);

    /**
     * Closes this GUI for the specified player.
     *
     * @param user the player to close the GUI for
     */
    void close(@NotNull User user);

    /**
     * Closes this GUI for all current viewers.
     */
    void close();

    @NotNull
    Component title();

    @Range(from = 0L, to = 54L)
    int size();

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
