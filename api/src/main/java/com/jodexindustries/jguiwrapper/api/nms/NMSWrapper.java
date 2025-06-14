package com.jodexindustries.jguiwrapper.api.nms;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NMSWrapper {

    boolean updateMenu(HumanEntity player, @Nullable InventoryType type, int size, Component title);

    boolean openInventory(HumanEntity player, @NotNull Inventory inventory, @NotNull InventoryType type, int size, Component title);
}
