package com.jodexindustries.jguiwrapper.minestom.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.jspecify.annotations.NonNull;

public class MinestomInventory extends Inventory {
    public MinestomInventory(InventoryType inventoryType, Component title) {
        super(inventoryType, title);
    }

    public MinestomInventory(InventoryType inventoryType, String title) {
        super(inventoryType, title);
    }

    @Override
    public boolean addViewer(@NonNull Player player) {
        // removed window packets
        return this.viewers.add(player);
    }
}
