package com.jodexindustries.jguiwrapper.minestom.gui;

import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.minestom.MinestomGuiApi;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface MinestomGui extends Gui {

    /**
     * Gets the holder for this GUI instance.
     *
     * @return the GuiHolder associated with this GUI
     */
    @NotNull
    MinestomGuiHolder holder();

    /**
     * Opens this GUI for the specified player.
     *
     * @param player the player to open the GUI for
     */
    default void open(@NotNull Player player) {
        open(player, MinestomGuiApi.get().user(player));
    }

    @Override
    default void open(@NotNull User user) {
        this.open(user.as(Player.class), user);
    }

    void open(@NotNull Player player, @NotNull User user);

    /**
     * Opens this GUI for the specified player with a custom title.
     *
     * @param player the player to open the GUI for
     * @param title  the custom title to use
     */
    default void open(@NotNull Player player, @NotNull Component title) {
        open(player, MinestomGuiApi.get().user(player), title);
    }

    @Override
    default void open(@NotNull User user, @NotNull Component title) {
        this.open(user.as(Player.class), title);
    }

    void open(@NotNull Player player, @NotNull User user, @NotNull Component title);

    /**
     * Closes this GUI for the specified player.
     *
     * @param player the player to close the GUI for
     */
    default void close(@NotNull Player player) {
        close(player, MinestomGuiApi.get().user(player));
    }

    @Override
    default void close(@NotNull User user) {
        this.close(user.as(Player.class), user);
    }

    void close(@NotNull Player player, @NotNull User user);

    /**
     * Closes this GUI for all current viewers.
     */
    @Override
    default void close() {
        new ArrayList<>(holder().getInventory().getViewers()).forEach(this::close);
    }
}
