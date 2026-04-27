package com.jodexindustries.jguiwrapper.paper.api.gui;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.paper.api.item.PaperItemWrapper;
import com.jodexindustries.jguiwrapper.paper.gui.AbstractGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

@SuppressWarnings({"unused"})
public class PaperGuiHolder implements GuiHolder<PaperItemWrapper>, InventoryHolder {

    private final AbstractGui<?> gui;
    private final Inventory inventory;

    public PaperGuiHolder(@NotNull AbstractGui<?> gui) {
        this(gui, null);
    }

    public PaperGuiHolder(@NotNull AbstractGui<?> gui, @Nullable InventoryType type) {
        this.gui = gui;
        int size = gui.size();
        Component title = gui.title();
        this.inventory = type != null ? Bukkit.createInventory(this, type, title) : Bukkit.createInventory(this, size, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public @NotNull AbstractGui<?> gui() {
        return gui;
    }

    @Override
    public void setItem(int slot, @NotNull PaperItemWrapper itemWrapper) {
        setItem(slot, itemWrapper.itemStack());
    }

    public void setItem(int slot, @NotNull ItemStack itemStack) {
        this.inventory.setItem(slot, itemStack);
    }

    @Override
    public void clear(@Range(from = 0L, to = 53L) int slot) {
        this.inventory.clear(slot);
    }

}
