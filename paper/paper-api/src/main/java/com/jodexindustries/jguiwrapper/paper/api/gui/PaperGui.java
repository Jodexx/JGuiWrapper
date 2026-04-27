package com.jodexindustries.jguiwrapper.paper.api.gui;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Represents a graphical user interface for Paper platforms
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
    default void open(@NotNull HumanEntity player) {
        open(player, PaperGuiApi.get().user(player));
    }

    @Override
    default void open(@NotNull User user) {
        open(user.as(HumanEntity.class), user);
    }

    void open(@NotNull HumanEntity player, @NotNull User user);

    /**
     * Opens this GUI for the specified player with a custom title.
     *
     * @param player the player to open the GUI for
     * @param title  the custom title to use
     */
    default void open(@NotNull HumanEntity player, @NotNull Component title) {
        open(player, PaperGuiApi.get().user(player), title);
    }

    @Override
    default void open(@NotNull User user, @NotNull Component title) {
        open(user.as(HumanEntity.class), user, title);
    }

    void open(@NotNull HumanEntity player, @NotNull User user, @NotNull Component title);

    /**
     * Closes this GUI for the specified player.
     *
     * @param player the player to close the GUI for
     */
    default void close(@NotNull HumanEntity player) {
        close(player, PaperGuiApi.get().user(player));
    }

    @Override
    default void close(@NotNull User user) {
        close(user.as(HumanEntity.class), user);
    }

    void close(@NotNull HumanEntity player, @NotNull User user);

    /**
     * Closes this GUI for all current viewers.
     */
    @Override
    default void close() {
        new ArrayList<>(holder().getInventory().getViewers()).forEach(this::close);
    }
}
