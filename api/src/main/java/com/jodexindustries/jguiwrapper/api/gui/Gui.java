package com.jodexindustries.jguiwrapper.api.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface Gui {

    @NotNull GuiHolder holder();

    @ApiStatus.Experimental
    void updateHolder();

    /**
     * @deprecated changing the title is not supported. This method has poorly defined and broken behaviors. It should not be used.
     * @since 1.20
     * @param title The new title.
     */
    @Deprecated(since = "1.21.1")
    default void setTitle(@NotNull HumanEntity player, @NotNull String title) {
        player.getOpenInventory().setTitle(title);
    }

    /**
     * @deprecated changing the title is not supported. This method has poorly defined and broken behaviors. It should not be used.
     * @since 1.20
     * @param title The new title.
     */
    @Deprecated(since = "1.21.1")
    default void setTitle(@NotNull String title) {
        holder().getInventory().getViewers().forEach(humanEntity -> setTitle(humanEntity, title));
    }

    default void open(@NotNull HumanEntity player) {
        player.openInventory(holder().getInventory());
    }

    default void close(@NotNull HumanEntity player) {
        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
    }

    default void close() {
        this.holder().getInventory().close();
    }
}
