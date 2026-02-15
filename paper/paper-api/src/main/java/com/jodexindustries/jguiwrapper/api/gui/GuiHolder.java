package com.jodexindustries.jguiwrapper.api.gui;

import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@SuppressWarnings({"unused"})
public class GuiHolder implements InventoryHolder {

    private final AbstractGui gui;

    private final int size;
    private final Component title;

    private final Inventory inventory;

    public GuiHolder(@NotNull AbstractGui gui) {
        this(gui, null);
    }

    public GuiHolder(@NotNull AbstractGui gui, @Nullable InventoryType type) {
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

    public @NotNull Component title() {
        return title;
    }

    @Range(from = 0L, to = 54L)
    public int size() {
        return size;
    }
}
