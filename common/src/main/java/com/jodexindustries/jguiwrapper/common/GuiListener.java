package com.jodexindustries.jguiwrapper.common;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getClickedInventory());
        if (holder == null) return;

        holder.getGui().onClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.getGui().onClose(e);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.getGui().onOpen(e);
    }

    @EventHandler
    public void onInventory(InventoryDragEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.getGui().onDrag(e);
    }
}
