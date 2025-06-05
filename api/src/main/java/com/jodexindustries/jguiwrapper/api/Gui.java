package com.jodexindustries.jguiwrapper.api;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface Gui {

    @NotNull GuiHolder holder();

    void onOpen(@NotNull InventoryOpenEvent event);

    void onClose(@NotNull InventoryCloseEvent event);

    void onClick(@NotNull InventoryClickEvent event);

    void onDrag(@NotNull InventoryDragEvent event);

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
