package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.gui.AbstractGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class TestGui extends AbstractGui {

    private int clicks;

    public TestGui() {
        super(3123, "&cExample");
    }

    @Override
    public void onOpen(@NotNull InventoryOpenEvent event) {
    }

    @Override
    public void onClose(@NotNull InventoryCloseEvent event) {
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        clicks++;
        title(String.valueOf(clicks));
        updateTitle();
    }

    @Override
    public void onDrag(@NotNull InventoryDragEvent event) {
    }
}
