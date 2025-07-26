package com.jodexindustries.jguiwrapper.common.gui;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.common.GuiUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

@SuppressWarnings({"unused"})
public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.gui().onClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getReason() == InventoryCloseEvent.Reason.PLUGIN) return;

        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.gui().onClose(e);
    }

    @EventHandler
    public void onInventory(InventoryDragEvent e) {
        GuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.gui().onDrag(e);
    }
}
