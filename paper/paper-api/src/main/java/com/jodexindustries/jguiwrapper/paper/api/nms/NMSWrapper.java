package com.jodexindustries.jguiwrapper.paper.api.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides NMS (net.minecraft.server) related operations for GUI management.
 * <p>
 * This interface abstracts version-dependent inventory operations such as updating and opening inventories
 * with custom types, sizes, and titles for players. Implementations should handle compatibility with different
 * Minecraft server versions.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface NMSWrapper {

    /**
     * Updates the player's currently open inventory menu with the specified type, size, and title.
     *
     * @param player the player whose menu should be updated
     * @param type   the inventory type to update to (nullable)
     * @param size   the new inventory size
     * @param title  the new inventory title as a Component
     * @return true if the menu was updated successfully, false otherwise
     */
    default boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title) {
        return updateMenu(player, type, size, title, false);
    }

    /**
     * Updates the player's currently open inventory menu with the specified type, size, and title.
     *
     * @param player      the player whose menu should be updated
     * @param type        the inventory type to update to (nullable)
     * @param size        the new inventory size
     * @param title       the new inventory title as a Component
     * @param refreshData whether to refresh the menu's data ({@code true} to refresh, {@code false} to keep existing data)
     * @return true if the menu was updated successfully, false otherwise
     */
    boolean updateMenu(@NotNull HumanEntity player, @Nullable InventoryType type, int size, @Nullable Component title, boolean refreshData);

    /**
     * Opens a new inventory for the player with the specified inventory, type, size, and title.
     *
     * @param player    the player to open the inventory for
     * @param inventory the inventory instance to open
     * @param type      the inventory type
     * @param size      the inventory size
     * @param title     the inventory title as a Component
     * @return the InventoryView if successful, or null if the operation failed
     */
    @Nullable
    InventoryView openInventory(@NotNull HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, @NotNull Component title);
}
