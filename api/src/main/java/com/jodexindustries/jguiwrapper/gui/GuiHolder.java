package com.jodexindustries.jguiwrapper.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class GuiHolder implements InventoryHolder {

    private final AbstractGui gui;
    private final Inventory inventory;

    public GuiHolder(AbstractGui gui) {
        this.gui = gui;
        this.inventory = Bukkit.createInventory(this, gui.size(), gui.title());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public AbstractGui getGui() {
        return gui;
    }
}
