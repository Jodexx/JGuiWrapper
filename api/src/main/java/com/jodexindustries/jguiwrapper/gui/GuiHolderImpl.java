package com.jodexindustries.jguiwrapper.gui;

import com.jodexindustries.jguiwrapper.api.GuiHolder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiHolderImpl implements GuiHolder {

    private final AbstractGui gui;
    private final Inventory inventory;

    public GuiHolderImpl(AbstractGui gui) {
        this.gui = gui;
        this.inventory = Bukkit.createInventory(this, gui.size(), gui.title());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public @NotNull AbstractGui getGui() {
        return gui;
    }
}
