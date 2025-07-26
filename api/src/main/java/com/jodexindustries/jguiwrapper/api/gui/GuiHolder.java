package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public class GuiHolder implements InventoryHolder {

    private final AbstractGui gui;

    private final int size;
    private final Component title;

    private final Inventory inventory;

    public GuiHolder(AbstractGui gui) {
        this(gui, null);
    }

    public GuiHolder(AbstractGui gui, InventoryType type) {
        this.gui = gui;
        this.size = gui.size();
        this.title = gui.title();
        this.inventory = type != null ? Bukkit.createInventory(this, type, title) : Bukkit.createInventory(this, size, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public @NotNull AbstractGui gui() {
        return gui;
    }

    public Component title() {
        return title;
    }

    public int size() {
        return size;
    }

}
