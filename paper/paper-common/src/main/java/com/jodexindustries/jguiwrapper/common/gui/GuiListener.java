package com.jodexindustries.jguiwrapper.common.gui;

import com.jodexindustries.jguiwrapper.paper.api.gui.PaperGuiHolder;
import com.jodexindustries.jguiwrapper.paper.utils.GuiUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

@SuppressWarnings({"unused"})
public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        PaperGuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.gui().onClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getReason() == InventoryCloseEvent.Reason.PLUGIN) return;

        PaperGuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.gui().onClose(e);
    }

    @EventHandler
    public void onInventory(InventoryDragEvent e) {
        PaperGuiHolder holder = GuiUtils.getHolder(e.getInventory());
        if (holder == null) return;

        holder.gui().onDrag(e);
    }
}
