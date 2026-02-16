package com.jodexindustries.jguiwrapper.paper.api.gui;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Represents a graphical user interface (GUI) instance.
 * Provides methods for opening, closing, and updating GUI holders for players.
 * Also manages active GUI instances for tracking and mass operations.
 */
@SuppressWarnings({"unused"})
public interface PaperGui extends Gui {

    /**
     * Gets the holder for this GUI instance.
     *
     * @return the GuiHolder associated with this GUI
     */
    @NotNull
    PaperGuiHolder holder();

    /**
     * Opens this GUI for the specified player.
     *
     * @param player the player to open the GUI for
     */
    void open(@NotNull HumanEntity player);

    @Override
    default void open(@NotNull Object player) {
        if (!(player instanceof HumanEntity)) {
            throw new IllegalArgumentException("Expected HumanEntity, got: " + player.getClass().getName());
        }
        this.open(((HumanEntity) player));
    }

    /**
     * Opens this GUI for the specified player with a custom title.
     *
     * @param player the player to open the GUI for
     * @param title  the custom title to use
     */
    void open(@NotNull HumanEntity player, @NotNull Component title);

    @Override
    default void open(@NotNull Object player, @NotNull Component title) {
        if (!(player instanceof HumanEntity)) {
            throw new IllegalArgumentException("Expected HumanEntity, got: " + player.getClass().getName());
        }
        this.open(((HumanEntity) player), title);
    }

    /**
     * Closes this GUI for the specified player.
     *
     * @param player the player to close the GUI for
     */
    void close(@NotNull HumanEntity player);

    @Override
    default void close(@NotNull Object player) {
        if (!(player instanceof HumanEntity)) {
            throw new IllegalArgumentException("Expected HumanEntity, got: " + player.getClass().getName());
        }
        this.close(((HumanEntity) player));
    }

    /**
     * Closes this GUI for all current viewers.
     */
    @Override
    default void close() {
        new ArrayList<>(holder().getInventory().getViewers()).forEach(this::close);
    }
}
