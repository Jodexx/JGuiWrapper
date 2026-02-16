package com.jodexindustries.jguiwrapper.paper.api.gui;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.paper.gui.AbstractGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@SuppressWarnings({"unused"})
public class PaperGuiHolder implements GuiHolder, InventoryHolder {

    private final AbstractGui gui;

    private final int size;
    private final Component title;

    private final Inventory inventory;

    public PaperGuiHolder(@NotNull AbstractGui gui) {
        this(gui, null);
    }

    public PaperGuiHolder(@NotNull AbstractGui gui, @Nullable InventoryType type) {
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

    @Override
    public @NotNull Component title() {
        return title;
    }

    @Override
    @Range(from = 0L, to = 54L)
    public int size() {
        return size;
    }
}
